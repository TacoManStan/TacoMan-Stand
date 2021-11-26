package com.taco.suit_lady.view.ui.console;

import com.taco.suit_lady.util.tools.BindingTools;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.jfx.lists.treehandler.TreeCellData;
import com.taco.suit_lady.view.ui.jfx.lists.treehandler.TreeLoader;
import com.taco.suit_lady.view.ui.jfx.lists.treehandler.WrappingTreeLoader;
import com.taco.suit_lady.view.ui.ui_internal.AppUI;
import com.taco.suit_lady.view.ui.ui_internal.console.ConsolePage;
import com.taco.suit_lady.view.ui.ui_internal.console.ConsoleUIDataContainer;
import com.taco.suit_lady.view.ui.ui_internal.controllers.ConsoleElementController;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.control.TreeCell;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

public class Console
{
    
    //<editor-fold desc="--- STATIC VARS ---">
    
    private static final String CONSOLE_ROOT_NAME;
    private static boolean initialized;
    
    static {
        CONSOLE_ROOT_NAME = "Console";
        initialized = false;
    }
    
    // </editor-fold>
    
    //
    
    //<editor-fold desc="--- INSTANCE FIELD VARS ---">
    
    private final ReentrantLock lock;
    
    private final ReadOnlyListWrapper<WrappingTreeLoader<ConsoleMessageable<?>, ConsoleElementController>> treeLoaders;
    private final ReadOnlyListWrapper<ConsoleMessageable<?>> messages;
    private final HashMap<Thread, StringBuilder> inProgressMap;
    
    private ConsolePage consolePage;
    
    //</editor-fold>
    
    //<editor-fold desc="--- CONSTRUCTORS ---">
    
    public Console()
    {
        this.lock = new ReentrantLock();
        
        this.treeLoaders = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        this.messages = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        this.inProgressMap = new HashMap<>();
        
    }
    //</editor-fold>
    
    //
    
    // <editor-fold desc="--- PROPERTIES ---">
    
    public ReadOnlyListProperty<ConsoleMessageable<?>> getMessages()
    {
        return messages.getReadOnlyProperty();
    }
    
    public boolean isInitialized()
    {
        return initialized;
    }
    
    public final ConsolePage getPage()
    {
        if (consolePage == null) // Lazy initialization
            consolePage = TB.resources().get("pages", "console");
        return consolePage;
    }
    
    private StringBuilder getInProgress()
    {
        return inProgressMap.computeIfAbsent(Thread.currentThread(), _thread -> new StringBuilder());
    }
    
    //
    
    public final ReadOnlyListProperty<WrappingTreeLoader<ConsoleMessageable<?>, ConsoleElementController>> getActiveConsoles()
    {
        return treeLoaders.getReadOnlyProperty();
    }
    
    // </editor-fold>
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    /**
     * <p>Initializes this {@link Console} object.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>This method is called internally by the {@link AppUI} object for this runtime instance upon its {@link AppUI#init() initialization}.</li>
     * </ol>
     * <p><b>Initialization Process</b></p>
     * <ol>
     *     <li>Locks the synchronization {@link ReentrantLock lock}.</li>
     *     <li>Throws an {@link ExceptionTools#ex(String) exception} if this {@link Console} has already been {@link #isInitialized() initialized}.</li>
     *     <li>Calls the <code><i>{@link #initStreams()}</i></code> method.</li>
     *     <li>
     *         Adds a {@link ListChangeListener} to the {@link #getMessages() Message List}:
     *         <ol>
     *             <li>{@link TreeLoader#generateCell(String, String, Function, Consumer) Generates} a new {@link TreeCellData} object for each {@link ConsoleMessageable} that was {@link ListChangeListener.Change#getAddedSubList() added} in the {@link ListChangeListener.Change Change} object.</li>
     *             <li>Clears the {@link #getMessages() Pending Messages List} after they have been added to the {@link Console} — as the last operation in the {@link ListChangeListener.Change List Change Response}.</li>
     *         </ol>
     *     </li>
     *     <li>Releases the synchronization {@link ReentrantLock lock}.</li>
     * </ol>
     */
    public final void initialize()
    {
        lock.lock();
        try {
            if (initialized)
                throw ExceptionTools.ex("Console has already been created.");
            initialized = true;
            
            initStreams();
            
            // TODO [S]: Move this functionality to TreeLoader.
            messages.addListener((ListChangeListener<ConsoleMessageable<?>>) change -> {
                final List<WrappingTreeLoader<ConsoleMessageable<?>, ConsoleElementController>> activeConsoleHandlers = getActiveConsoles();
                while (change.next())
                    if (change.wasAdded())
                        activeConsoleHandlers.forEach(treeHandler -> FXTools.get().runFX(
                                () -> change.getAddedSubList().forEach(
                                        message -> treeHandler.generateCell(
                                                CONSOLE_ROOT_NAME,
                                                objs -> message
                                        )), true));
                
                // Clear the pending messages as they've already been added to the console.
                if (!messages.isEmpty())
                    messages.clear();
            });
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * <p>Initializes the default {@link PrintStream Print Streams} to duplicate their output to this {@code JavaFX} {@link Console} instance.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>This method is called internally by the <code><i>{@link #initialize()}</i></code> method.</li>
     *     <li>Synchronization is done in the <code><i>{@link #initialize()}</i></code> method.</li>
     * </ol>
     *
     * @see ConsoleOutputStream
     * @see PrintStream
     * @see System#setOut(PrintStream)
     * @see System#setErr(PrintStream)
     */
    private void initStreams()
    {
        // Synchronization done in initialize()
        
        ConsoleOutputStream outputConsole = new ConsoleOutputStream(System.out);
        ConsoleOutputStream errorConsole = new ConsoleOutputStream(System.err);
        PrintStream outputStream = new ConsolePrintStream(outputConsole, System.out, true);
        PrintStream errorStream = new PrintStream(errorConsole, true);
        
        System.setOut(outputStream);
        System.setErr(errorStream);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- HELPER METHODS ---">
    
    private void append(String str)
    {
        // CHANGE-HERE
        FXTools.get().runFX(() -> messages.add(new SimpleConsoleMessage(str)), false);
    }
    
    //</editor-fold>
    
    //
    
    //<editor-fold desc="--- STATIC ---">
    
    public static void consolify(FxWeaver weaver, ConfigurableApplicationContext ctx, ConsoleUIDataContainer consoleContainer)
    {
        ExceptionTools.nullCheck(consoleContainer, "Console UI Data Container");
        
        final Console console = TB.console();
        FXTools.get().runFX(() -> {
            // treeView.setShowRoot(false); // Disabled temporarily because for some reason hiding the root causes messages to be truncated.
            
            // The below binding is used to trigger a refresh whenever a console display checkbox is toggled.
            final IntegerBinding incrementingBinding = BindingTools.incrementingBinding(
                    consoleContainer.showTRiBotProperty(),
                    consoleContainer.showClientProperty(),
                    consoleContainer.showScriptProperty(),
                    consoleContainer.showSelectedInstanceOnlyProperty(),
                    TB.handler().selectedInstanceProperty()
            );
            
            final WrappingTreeLoader<ConsoleMessageable<?>, ConsoleElementController> treeLoader = new WrappingTreeLoader<>(
                    consoleContainer.getTreeView(),
                    cellData -> weaver.loadController(ConsoleElementController.class),
                    consoleContainer.getValidator(),
                    CONSOLE_ROOT_NAME
            ).initializeAndGet();
            
            incrementingBinding.addListener((observable, oldValue, newValue) -> treeLoader.revalidate()); // currently unused
            
            console.treeLoaders.add(treeLoader);
        }, true);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INNER CLASSES ---">
    
    private class ConsolePrintStream extends PrintStream
    {
        private final PrintStream system_stream;
        
        private ConsolePrintStream(final OutputStream output_stream, final PrintStream system_stream)
        {
            super(output_stream);
            this.system_stream = system_stream;
        }
        
        private ConsolePrintStream(final OutputStream output_stream, final PrintStream system_stream, final boolean auto_flush)
        {
            super(output_stream, auto_flush);
            this.system_stream = system_stream;
        }
    }
    
    private class ConsoleOutputStream extends OutputStream
    {
        private final String LINE_SEPARATOR = System.getProperty("line.separator");
        
        private final PrintStream nativeStream;
        // private char previousCharacter;
        
        private int buffer_len;
        private byte[] buffer;
        
        private int count;
        
        private ConsoleOutputStream(PrintStream nativeStream)
        {
            this.nativeStream = nativeStream;
            // this.previousCharacter = ' ';
            
            this.buffer_len = 2048;
            this.buffer = new byte[this.buffer_len];
            
            this.count = 0;
        }
        
        /*
         * @Override public void write(int b) throws IOException {
         * nativeStream.write(b); // Write to the default console
         * Console.this.append(b); // Write to the custom console }
         */
        
        @Override
        public void write(final int b) throws IOException
        {
            this.nativeStream.write(b);
            
            if (b == 0)
                return;
            
            if (this.count == this.buffer_len) {
                int new_buffer_len = this.buffer_len + 2048;
                byte[] new_buffer = new byte[new_buffer_len];
                
                System.arraycopy(this.buffer, 0, new_buffer, 0, this.buffer_len);
                
                this.buffer = new_buffer;
                this.buffer_len = new_buffer_len;
            }
            
            this.buffer[this.count++] = (byte) b;
        }
        
        @Override
        public void flush()
        {
            this.nativeStream.flush();
            
            if (this.count == 0)
                return;
            
            if ((this.count == LINE_SEPARATOR.length()) && ((char) this.buffer[0] == LINE_SEPARATOR.charAt(0))
                && (((this.count == 1)
                     || ((this.count == 2) && ((char) this.buffer[1] == LINE_SEPARATOR.charAt(1)))))) {
                reset();
                
                return;
            }
            
            final byte[] bytes = new byte[this.count];
            
            System.arraycopy(this.buffer, 0, bytes, 0, this.count);
            
            final String str = new String(bytes);
            
            Console.this.append(str);
            
            reset();
        }
        
        private void reset()
        {
            this.count = 0;
        }
        
    }
    
    //
    
    // DO NOT DELETE THIS (for now) because you might want to enable it as a "lite mode" option.
    private static class ConsoleCellImpl extends TreeCell<ConsoleMessageable<?>>
    {
        // @Override public void startEdit() {
        // super.startEdit();
        // }
        //
        // @Override public void commitEdit(ConsoleMessageable newValue) {
        // super.commitEdit(newValue);
        // }
        //
        // @Override public void cancelEdit() {
        // super.cancelEdit();
        // }
        //
        
        @Override
        protected void updateItem(ConsoleMessageable<?> item, boolean empty)
        {
            super.updateItem(item, empty);
            
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.getText().toString());
            }
        }
    }
    
    //</editor-fold>
}

/*
 * TODO LIST:
 * [S] Allow for advanced messages to be formatted via HTML, or at the minimum, have italics/bold/underline support.
 * [S] Add support for printing Maps, i.e., easily viewable keys and maps.
 * [S] Update synchronization in messages listener to ensure that all messages from the same source are printed in sequence.
 * [S] Figure out why messages are truncated only when the root is hidden.
 */