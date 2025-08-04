package br.edu.ifba.inf008.interfaces;


public interface IPlugin
{
    boolean init();
    String getId();
    String getCapabilities();
}
