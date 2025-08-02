package br.edu.ifba.inf008.interfaces;

public abstract class ICore
{
    public abstract IUIController getUIController();
    public abstract IAuthenticationController getAuthenticationController();
    public abstract IIOController getIOController();
    public abstract IPluginController getPluginController();
    public abstract IDatabaseService getDatabaseService();
}