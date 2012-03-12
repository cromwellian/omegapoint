package com.artemis;

import com.artemis.utils.Bag;
import com.artemis.utils.EntityDebugger;
import com.artemis.utils.ImmutableBag;

public class EntityManager {
    private World world;
    private Bag<Entity> activeEntities;
    private Bag<Entity> removedAndAvailable;
    private int nextAvailableId;
    private int count;
    private long uniqueEntityId;
    private long totalCreated;
    private long totalRemoved;

    private Bag<Bag<? extends Component>> componentsByType;

    private Bag<Component> entityComponents; // Added for debug support.

    public EntityManager(World world) {
        this.world = world;

        activeEntities = new Bag<Entity>();
        removedAndAvailable = new Bag<Entity>();

        componentsByType = new Bag<Bag<? extends Component>>(64);

        entityComponents = new Bag<Component>();
    }

    protected Entity create() {
        //Entity e = removedAndAvailable.removeLast();
        //if (e == null) {
        Entity e = new Entity(world, nextAvailableId++);
        //    System.out.println("New entity : " + EntityDebugger.getEntityString(e));
        //} else {
        //    System.out.println("Reusing : " + EntityDebugger.getEntityString(e));
        //    e.reset();
        // }
        e.setUniqueId(uniqueEntityId++);
        activeEntities.add(e);
        count++;
        totalCreated++;

        return e;
    }

    protected void remove(Entity e) {
        if (removedAndAvailable.contains(e)) {
            System.out.println(EntityDebugger.getEntityString(e));
            throw new IllegalStateException("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ Removed and available already contains entity, this is usually not a good sign!");
        }

        activeEntities.set(e.getId(), null);

        e.setTypeBits(0);

        refresh(e);

        removeComponentsOfEntity(e);

        count--;
        totalRemoved++;

        removedAndAvailable.add(e);
    }

    private void removeComponentsOfEntity(Entity e) {
        for (int a = 0; componentsByType.size() > a; a++) {
            Bag<? extends Component> components = componentsByType.get(a);
            if (components != null && e.getId() < components.size()) {
                components.set(e.getId(), null);
            }
        }
    }

    /**
     * Check if this entity is active, or has been deleted, within the framework.
     *
     * @param entityId
     * @return active or not.
     */
    public boolean isActive(int entityId) {
        return activeEntities.get(entityId) != null;
    }

    protected <T extends Component> void addComponent(Entity e, T component) {
        ComponentType type = ComponentTypeManager.getTypeFor(component.getClass());

        if (type.getId() >= componentsByType.size()) {
            componentsByType.set(type.getId(), null);
        }

        Bag<T> components = (Bag<T>) componentsByType.get(type.getId());
        if (components == null) {
            components = new Bag<T>();
            componentsByType.set(type.getId(), components);
        }

        components.set(e.getId(), component);

        e.addTypeBit(type.getBit());
    }

    protected void refresh(Entity e) {
        SystemManager systemManager = world.getSystemManager();
        Bag<EntitySystem> systems = systemManager.getSystems();
        for (int i = 0, s = systems.size(); s > i; i++) {
            systems.get(i).change(e);
        }
    }

    protected void removeComponent(Entity e, Component component) {
        ComponentType type = ComponentTypeManager.getTypeFor(component.getClass());
        removeComponent(e, type);
    }

    protected void removeComponent(Entity e, ComponentType type) {
        Bag<? extends Component> components = componentsByType.get(type.getId());
        components.set(e.getId(), null);
        e.removeTypeBit(type.getBit());
    }

    protected <T extends Component> T getComponent(Entity e, ComponentType type) {
        Bag<? extends Component> bag = componentsByType.get(type.getId());
        if (bag != null && e.getId() < bag.size())
            return (T) bag.get(e.getId());
        return null;
    }

    protected Component getComponentUntyped(Entity e, ComponentType type) {
        Bag<? extends Component> bag = componentsByType.get(type.getId());
        if (bag != null && e.getId() < bag.size())
            return bag.get(e.getId());
        return null;
    }


    /**
     * @return how many entities are currently active.
     */
    public int getEntityCount() {
        return count;
    }

    /**
     * @return how many entities have been created since start.
     */
    public long getTotalCreated() {
        return totalCreated;
    }

    /**
     * @return how many entities have been removed since start.
     */
    public long getTotalRemoved() {
        return totalRemoved;
    }

    protected ImmutableBag<Component> getComponents(Entity e) {
        entityComponents.clear();
        for (int i = 0; i < componentsByType.size(); i++) {
            final Bag<? extends Component> components = componentsByType.get(i);
            if (components != null && e.getId() < components.size()) {
                Component component = components.get(e.getId());
                if (component != null) {
                    entityComponents.add(component);
                }
            }
        }
        return entityComponents;
    }

}
