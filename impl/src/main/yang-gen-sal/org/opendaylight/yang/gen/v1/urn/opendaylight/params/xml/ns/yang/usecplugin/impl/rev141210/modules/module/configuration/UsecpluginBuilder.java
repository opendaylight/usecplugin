package org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration;
import java.util.Collections;
import java.util.Map;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.usecplugin.Broker;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.AugmentationHolder;
import java.util.HashMap;
import org.opendaylight.yangtools.concepts.Builder;
import org.opendaylight.yangtools.yang.binding.Augmentation;


/**
 * Class that builds {@link org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin} instances.
 *
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin
 *
 */
public class UsecpluginBuilder implements Builder <org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin> {

    private Broker _broker;

    Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin>> augmentation = Collections.emptyMap();

    public UsecpluginBuilder() {
    }

    public UsecpluginBuilder(Usecplugin base) {
        this._broker = base.getBroker();
        if (base instanceof UsecpluginImpl) {
            UsecpluginImpl impl = (UsecpluginImpl) base;
            if (!impl.augmentation.isEmpty()) {
                this.augmentation = new HashMap<>(impl.augmentation);
            }
        } else if (base instanceof AugmentationHolder) {
            @SuppressWarnings("unchecked")
            AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin> casted =(AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin>) base;
            if (!casted.augmentations().isEmpty()) {
                this.augmentation = new HashMap<>(casted.augmentations());
            }
        }
    }


    public Broker getBroker() {
        return _broker;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin>> E getAugmentation(java.lang.Class<E> augmentationType) {
        if (augmentationType == null) {
            throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
        }
        return (E) augmentation.get(augmentationType);
    }

    public UsecpluginBuilder setBroker(Broker value) {
        this._broker = value;
        return this;
    }
    
    public UsecpluginBuilder addAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin>> augmentationType, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin> augmentation) {
        if (augmentation == null) {
            return removeAugmentation(augmentationType);
        }
    
        if (!(this.augmentation instanceof HashMap)) {
            this.augmentation = new HashMap<>();
        }
    
        this.augmentation.put(augmentationType, augmentation);
        return this;
    }
    
    public UsecpluginBuilder removeAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin>> augmentationType) {
        if (this.augmentation instanceof HashMap) {
            this.augmentation.remove(augmentationType);
        }
        return this;
    }

    public Usecplugin build() {
        return new UsecpluginImpl(this);
    }

    private static final class UsecpluginImpl implements Usecplugin {

        public java.lang.Class<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin> getImplementedInterface() {
            return org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin.class;
        }

        private final Broker _broker;

        private Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin>> augmentation = Collections.emptyMap();

        private UsecpluginImpl(UsecpluginBuilder base) {
            this._broker = base.getBroker();
            switch (base.augmentation.size()) {
            case 0:
                this.augmentation = Collections.emptyMap();
                break;
            case 1:
                final Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin>> e = base.augmentation.entrySet().iterator().next();
                this.augmentation = Collections.<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin>>singletonMap(e.getKey(), e.getValue());
                break;
            default :
                this.augmentation = new HashMap<>(base.augmentation);
            }
        }

        @Override
        public Broker getBroker() {
            return _broker;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin>> E getAugmentation(java.lang.Class<E> augmentationType) {
            if (augmentationType == null) {
                throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
            }
            return (E) augmentation.get(augmentationType);
        }

        private int hash = 0;
        private volatile boolean hashValid = false;
        
        @Override
        public int hashCode() {
            if (hashValid) {
                return hash;
            }
        
            final int prime = 31;
            int result = 1;
            result = prime * result + ((_broker == null) ? 0 : _broker.hashCode());
            result = prime * result + ((augmentation == null) ? 0 : augmentation.hashCode());
        
            hash = result;
            hashValid = true;
            return result;
        }

        @Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof DataObject)) {
                return false;
            }
            if (!org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin.class.equals(((DataObject)obj).getImplementedInterface())) {
                return false;
            }
            org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin other = (org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin)obj;
            if (_broker == null) {
                if (other.getBroker() != null) {
                    return false;
                }
            } else if(!_broker.equals(other.getBroker())) {
                return false;
            }
            if (getClass() == obj.getClass()) {
                // Simple case: we are comparing against self
                UsecpluginImpl otherImpl = (UsecpluginImpl) obj;
                if (augmentation == null) {
                    if (otherImpl.augmentation != null) {
                        return false;
                    }
                } else if(!augmentation.equals(otherImpl.augmentation)) {
                    return false;
                }
            } else {
                // Hard case: compare our augments with presence there...
                for (Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin>> e : augmentation.entrySet()) {
                    if (!e.getValue().equals(other.getAugmentation(e.getKey()))) {
                        return false;
                    }
                }
                // .. and give the other one the chance to do the same
                if (!obj.equals(this)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public java.lang.String toString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder ("Usecplugin [");
            boolean first = true;
        
            if (_broker != null) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append("_broker=");
                builder.append(_broker);
             }
            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }
            builder.append("augmentation=");
            builder.append(augmentation.values());
            return builder.append(']').toString();
        }
    }

}
