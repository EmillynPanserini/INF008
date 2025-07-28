package br.edu.ifba.inf008.interfaces;

import br.edu.ifba.inf008.interfaces.ICore;

public interface IPlugin
{
    public abstract boolean init();
    String getId(); //Permite q o PluginController registre o plugin pelo ID
    String getCapabilities();  // Descrição do que o plugin faz
}
