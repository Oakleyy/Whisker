package ninja.oakley.whisker;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.XMLBuilderParameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.xpath.XPathExpressionEngine;

public final class Configuration {

    private final long timeAccessed;

    private final String systemUser;
    private final String systemGroup;

    public Configuration() throws ConfigurationException {
        XMLConfiguration config = loadConfiguration();
        this.timeAccessed = System.currentTimeMillis();
        this.systemUser = config.getString("systemUser", "pi");
        this.systemGroup = config.getString("systemGroup", "pi");
    }
    
    public String getSystemUser(){
        return this.systemUser;
    }
    
    public String getSystemGroup(){
        return this.systemGroup;
    }
    
    public long getTimeAccessed(){
        return this.timeAccessed;
    }

    private XMLConfiguration loadConfiguration() throws ConfigurationException {
        Parameters params = new Parameters();
        
        XMLBuilderParameters xmlParams = params.xml()
                .setThrowExceptionOnMissing(true)
                .setValidating(true)
                .setEncoding("UTF-8")
                .setFileName("config.xml")
                .setExpressionEngine(new XPathExpressionEngine());
        
        FileBasedConfigurationBuilder<XMLConfiguration> builder =
                new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
                .configure(xmlParams);
        
        XMLConfiguration config = builder.getConfiguration();
        return config;
    }
}
