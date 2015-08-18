package org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.usecplugin.Broker;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.controller.config.rev130405.modules.module.Configuration;
import org.opendaylight.yangtools.yang.binding.Augmentable;


/**
 * &lt;p&gt;This class represents the following YANG schema fragment defined in module &lt;b&gt;usecplugin-impl&lt;/b&gt;
 * &lt;br&gt;(Source path: &lt;i&gt;META-INF/yang/usecplugin-impl.yang&lt;/i&gt;):
 * &lt;pre&gt;
 * case usecplugin {
 *     container broker {
 *         leaf type {
 *             type leafref;
 *         }
 *         leaf name {
 *             type leafref;
 *         }
 *         uses service-ref {
 *             refine (urn:opendaylight:params:xml:ns:yang:usecplugin:impl?revision=2014-12-10)type {
 *                 leaf type {
 *                     type leafref;
 *                 }
 *             }
 *         }
 *     }
 * }
 * &lt;/pre&gt;
 * The schema path to identify an instance is
 * &lt;i&gt;usecplugin-impl/modules/module/configuration/(urn:opendaylight:params:xml:ns:yang:usecplugin:impl?revision=2014-12-10)usecplugin&lt;/i&gt;
 *
 */
public interface Usecplugin
    extends
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.usecplugin.impl.rev141210.modules.module.configuration.Usecplugin>,
    Configuration
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.cachedReference(org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:params:xml:ns:yang:usecplugin:impl","2014-12-10","usecplugin"));

    Broker getBroker();

}

