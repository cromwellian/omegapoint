package com.artemis;

/**
 * High performance component retrieval from entities. Use this wherever you need
 * to retrieve components from entities often and fast.
 *
 * @param <T>
 * @author Arni Arent
 */
public class ComponentMapper<T extends Component> {
    private ComponentType type;
    private EntityManager em;
    private Class<T> classType;

    public ComponentMapper(Class<T> type, World world) {
        this.em = world.getEntityManager();
        this.type = ComponentTypeManager.getTypeFor(type);
        this.classType = type;
    }

    @SuppressWarnings("unchecked")
    public T get(Entity e) {
        return (T) em.getComponent(e, type);
    }

}
