package br.edu.ifba.inf008.infrastructure.shell;

import br.edu.ifba.inf008.App;
import br.edu.ifba.inf008.interfaces.IPluginController;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IRequiresDatabaseService;
import br.edu.ifba.inf008.interfaces.IDatabaseService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy; // Importar @Lazy

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;

@Component
public class PluginController implements IPluginController
{

    @Autowired
    @Lazy
    private ICore core;


    public PluginController() {

    }

    public boolean init() {
        try {
            File currentDir = new File("./plugins");

            FilenameFilter jarFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".jar");
                }
            };

            String []plugins = currentDir.list(jarFilter);
            int i;
            URL[] jars = new URL[plugins.length];
            for (i = 0; i < plugins.length; i++)
            {
                jars[i] = (new File("./plugins/" + plugins[i])).toURL();
            }
            URLClassLoader ulc = new URLClassLoader(jars, App.class.getClassLoader());
            for (i = 0; i < plugins.length; i++)
            {
                String pluginName = plugins[i].split("\\.")[0];
                IPlugin plugin = (IPlugin) Class.forName("br.edu.ifba.inf008.plugins." + pluginName, true, ulc).newInstance();

                if (plugin instanceof IRequiresDatabaseService) {
                    IDatabaseService dbService = core.getDatabaseService();
                    if (dbService == null) {
                        System.err.println("PluginController: Failed to get DatabaseService for " + pluginName + ".");
                        return false;
                    }
                    ((IRequiresDatabaseService) plugin).setDatabaseService(dbService);
                }

                plugin.init();
            }

            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getClass().getName() + " - " + e.getMessage());

            return false;
        }
    }

    @Override
    public IPlugin getPlugin(String id) {
        return null;
    }
}