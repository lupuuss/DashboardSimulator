package p.lodz.dashboardsimulator.base;

/**
 * Contains all necessary model implementations for given module.
 */
public interface Injector {

    /**
     * Creates all instances required by its module.
     * @param parentInjector Parent injector that shares some instances with this injector or is requried to correct creation of its instances.
     */
    void init(Injector parentInjector);
}
