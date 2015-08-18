package org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.usecplugin;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.controller.config.rev130405.ServiceRef;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.binding.ChildOf;
import org.opendaylight.yangtools.yang.binding.Augmentable;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.controller.config.rev130405.modules.Module;


/**
 * &lt;p&gt;This class represents the following YANG schema fragment defined in module &lt;b&gt;usecplugin-impl&lt;/b&gt;
 * &lt;br&gt;(Source path: &lt;i&gt;META-INF/yang/usecplugin-impl.yang&lt;/i&gt;):
 * &lt;pre&gt;
 * container broker {
 *     leaf type {
 *         type leafref;
 *     }
 *     leaf name {
 *         type leafref;
 *     }
 *     uses service-ref {
 *         refine (urn:opendaylight:params:xml:ns:yang:usecplugin:impl?revision=2014-12-10)type {
 *             leaf type {
 *                 type leafref;
 *             }
 *         }
 *     }
 * }
 * &lt;/pre&gt;
 * The schema path to identify an instance is
 * &lt;i&gt;usecplugin-impl/modules/module/configuration/(urn:opendaylight:params:xml:ns:yang:usecplugin:impl?revision=2014-12-10)usecplugin/broker&lt;/i&gt;
 *
 * &lt;p&gt;To create instances of this class use {@link org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.usecplugin.BrokerBuilder}.
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.usecplugin.BrokerBuilder
 *
 */
public interface Broker
    extends
    ChildOf<Module>,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.usecplugin.Broker>,
    ServiceRef
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.cachedReference(org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:params:xml:ns:yang:usecplugin:impl","2014-12-10","broker"));


}

