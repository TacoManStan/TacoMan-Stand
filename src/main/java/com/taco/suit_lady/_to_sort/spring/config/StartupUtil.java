package com.taco.suit_lady._to_sort.spring.config;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Arrays;

public final class StartupUtil
{
    public static class ROOT
    {
        public static class XML
        {
            public static final String prefix = "/beans/";
            
            /**
             * <p><b>Returns the names of all {@code XML Configuration Files} to be injected by the {@link ConfigurableApplicationContext Root Application Context}.</b></p>
             * <br>
             * <hr>
             * <p><b>Details</b></p>
             * <ul>
             *     <li>
             *         <p>All {@link Bean Beans} injected by the {@link ConfigurableApplicationContext Root Context} are globally accessible.</p>
             *         <ul>
             *             <li>Implementations of {@link ApplicationContextAware} expose all {@link Bean Beans} injected using the {@code XML Configurations} defined by this method.</li>
             *             <li>Implementations of {@link ApplicationContextAware} do *NOT* expose *ANY* {@link Bean Beans} injected using {@code XML Configurations} defined outside this method.</li>
             *         </ul>
             *     </li>
             *     <li>
             *         <p>Additional {@link ClassPathXmlApplicationContext XML Configurations} can be defined elsewhere to restrict XML-Injected Bean visibility to more specific scopes.</p>
             *         <ul>
             *             <li>All global XML-Injected {@link Bean Beans} *MUST* be defined by this method.</li>
             *             <li>{@link ClassPathXmlApplicationContext XML Configurations} defined outside this method must *NOT* be accessible in the {@link ConfigurableApplicationContext Root Scope}.</li>
             *         </ul>
             *     </li>
             * </ul>
             * <hr>
             * <p><b>XML Configuration Integration Options</b></p>
             * <ul>
             *     <li>Pass XML {@link ClassPathXmlApplicationContext#setConfigLocations(String...) File Names} to an {@link ClassPathXmlApplicationContext XML Context}.</li>
             *     <li>Parse {@code Raw XML} into a {@link GenericApplicationContext Generic Application Context} using a stand-alone {@link XmlBeanDefinitionReader XML Reader}.</li>
             *     <li>
             *         <p>Parse {@code Raw XML} into an {@link XmlReaderContext} using an enclosed {@link XmlBeanDefinitionReader XML Reader}.</p>
             *         <br>
             *         <p><b>{@link XmlReaderContext} Information</b></p>
             *         <ul>
             *             <li>{@link XmlReaderContext} does *NOT* implement {@link ApplicationContext}.</li>
             *             <li>i.e., {@link XmlReaderContext} can *NOT* be used as an {@link ApplicationContext}.</li>
             *             <li>{@link XmlReaderContext Reader Context} *DOES* offer additional {@code functionality} and {@code flexibility} to {@link XmlBeanDefinitionReader XML Reader}.</li>
             *             <li>{@link XmlReaderContext}, in turn, *DOES* grant improved control over parsing {@code Raw XML} into an {@link ApplicationContext}.</li>
             *         </ul>
             *     </li>
             * </ul>
             * <hr>
             * <br>
             *
             * @return An array of XML file names ready to be used to construct a {@link ClassPathXmlApplicationContext XML Context}.
             */
            public static String[] ctx_config()
            {
                /*
                You are either going to have to figure out how to allow XML-configured beans to access the root context, assert that all XML-configured beans will not have access to beans
                configured by non-XML methods, or avoid using XML-configured beans altogether (at least for now).
                 */
                final String[] fileNames = new String[]{
                        "beans"
                };
                
                return Arrays.stream(fileNames).map(
                        fileName -> prefix + fileName + ".xml"
                ).toArray(String[]::new);
            }
        }
    }
}
