package com.taco.suit_lady.view.ui.console;

import com.taco.suit_lady.util.BindingTools;
import com.taco.suit_lady.util.ExceptionTools;
import com.taco.suit_lady.util.TB;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.jfx.lists.treehandler.WrappingTreeLoader;
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

public class Console {

	//<editor-fold desc="--- STATIC VARS ---">

	private static final String CONSOLE_ROOT_NAME;
	private static boolean created;

	static {
		CONSOLE_ROOT_NAME = "Console";
		created = false;
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
	
	public Console() {
		this.lock = new ReentrantLock();

		this.treeLoaders = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
		this.messages = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
		this.inProgressMap = new HashMap<>();
		
	}
	//</editor-fold>
	
	//

	// <editor-fold desc="--- PROPERTIES ---">

	public final ConsolePage getPage() {
			if (consolePage == null) // Lazy initialization
				consolePage = TB.resources().get("pages", "console");
			return consolePage;
	}

	private StringBuilder getInProgress() {
		return inProgressMap.computeIfAbsent(Thread.currentThread(), _thread -> new StringBuilder());
	}

	//

	public final ReadOnlyListProperty<WrappingTreeLoader<ConsoleMessageable<?>, ConsoleElementController>> getActiveConsoles() {
		return treeLoaders.getReadOnlyProperty();
	}

	// </editor-fold>
	
	//<editor-fold desc="--- INITIALIZATION ---">
	
	public final void initialize() {
		initStreams();
		// TODO [S]: Move this functionality to TreeLoader.
		messages.addListener((ListChangeListener<ConsoleMessageable<?>>) _change -> {
			List<WrappingTreeLoader<ConsoleMessageable<?>, ConsoleElementController>> _activeConsoleHandlers = getActiveConsoles();
			while (_change.next())
				if (_change.wasAdded())
					_activeConsoleHandlers.forEach(_treeHandler -> {
//						Printing.dev("Tree Handler: " + _treeHandler);
						List<? extends ConsoleMessageable<?>> _added = _change.getAddedSubList();
						FXTools.get().runFX(() -> _added.forEach(_message -> {
//							Printing.dev("Message: " + _message);
							_treeHandler.generateCell(CONSOLE_ROOT_NAME, _objs -> _message);
						}), true);
					});

			// Clear the pending messages as they've already been added to the console.
			if (!messages.isEmpty())
				messages.clear();
		});
	}

	private void initStreams() {
		// Synchronization done in initialize()
		if (created)
			throw ExceptionTools.ex("Console has already been created.");
		created = true;

		ConsoleOutputStream outputConsole = new ConsoleOutputStream(System.out);
		ConsoleOutputStream errorConsole = new ConsoleOutputStream(System.err);
		PrintStream outputStream = new ConsolePrintStream(outputConsole, System.out, true);
		PrintStream errorStream = new PrintStream(errorConsole, true);

		System.setOut(outputStream);
		System.setErr(errorStream);
	}
	
	//</editor-fold>
	
	//<editor-fold desc="--- HELPER METHODS ---">
	
	private void append(String str) {
		// CHANGE-HERE
		FXTools.get().runFX(() -> messages.add(new SimpleConsoleMessage(str)), false);
	}
	
	//</editor-fold>
	
	//
	
	//<editor-fold desc="--- STATIC ---">
	
	public static void consolify(FxWeaver weaver, ConfigurableApplicationContext ctx, ConsoleUIDataContainer consoleContainer) {
		ExceptionTools.nullCheck(consoleContainer, "Console UI Data Container");
		
		final Console console = TB.console();
		FXTools.get().runFX(() -> {
			// treeView.setShowRoot(false); // Disabled temporarily because for some reason hiding the root causes messages to be truncated.

			// The below binding is used to trigger a refresh whenever a console display checkbox is toggled.
			final IntegerBinding incrementingBinding = BindingTools.get().incrementingBinding(
					consoleContainer.showTRiBotProperty(),
					consoleContainer.showClientProperty(),
					consoleContainer.showScriptProperty(),
					consoleContainer.showSelectedInstanceOnlyProperty(),
					TB.handler().selectedInstanceProperty());
			
			final WrappingTreeLoader<ConsoleMessageable<?>, ConsoleElementController> treeLoader = new WrappingTreeLoader<>(
					consoleContainer.getTreeView(),
					_cellData -> weaver.loadController(ConsoleElementController.class),
					consoleContainer.getValidator(),
					CONSOLE_ROOT_NAME
			).initializeAndGet();

			incrementingBinding.addListener((observable, oldValue, newValue) -> treeLoader.revalidate()); // currently unused
			
			console.treeLoaders.add(treeLoader);
		}, true);
	}
	
	//</editor-fold>
	
	//<editor-fold desc="--- INNER CLASSES ---">
	
	private class ConsolePrintStream extends PrintStream {

		private final PrintStream system_stream;

		private ConsolePrintStream(final OutputStream output_stream, final PrintStream system_stream) {
			super(output_stream);
			this.system_stream = system_stream;
		}

		private ConsolePrintStream(final OutputStream output_stream, final PrintStream system_stream, final boolean auto_flush) {
			super(output_stream, auto_flush);
			this.system_stream = system_stream;
		}
	}

	private class ConsoleOutputStream extends OutputStream {

		private final String LINE_SEPARATOR = System.getProperty("line.separator");

		private final PrintStream nativeStream;
		// private char previousCharacter;

		private int buffer_len;
		private byte[] buffer;

		private int count;

		private ConsoleOutputStream(PrintStream nativeStream) {
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

		@Override public void write(final int b) throws IOException {
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

		@Override public void flush() {
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

		private void reset() {
			this.count = 0;
		}

	}

	//

	// DO NOT DELETE THIS (for now) because you might want to enable it as a "lite mode" option.
	private static class ConsoleCellImpl extends TreeCell<ConsoleMessageable<?>> {

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
		protected void updateItem(ConsoleMessageable<?> item, boolean empty) {
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