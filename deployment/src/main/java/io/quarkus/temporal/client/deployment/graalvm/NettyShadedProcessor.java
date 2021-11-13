package io.quarkus.temporal.client.deployment.graalvm        //TODO: temporal workflow need register here?
        ;

import io.grpc.NameResolverProvider;
import io.grpc.internal.DnsNameResolverProvider;
import io.grpc.internal.ReadableBuffers;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelProvider;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.deployment.builditem.nativeimage.RuntimeInitializedClassBuildItem;
import io.quarkus.deployment.builditem.nativeimage.RuntimeInitializedPackageBuildItem;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;

import java.util.Collection;

/**
 * @author netodevel
 * This class configure netty-shaded to run on GraalVM
 */
public class NettyShadedProcessor {

    private static final String FEATURE = "netty-shaded";

    static final DotName NAME_RESOLVER_PROVIDER = DotName.createSimple(NameResolverProvider.class.getName());

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void registerReflecttionsNettyShaded(BuildProducer<ReflectiveClassBuildItem> reflections, CombinedIndexBuildItem combinedIndex) {
        ReflectiveClassBuildItem buildItem = new ReflectiveClassBuildItem(true, true, true,
                "io.grpc.netty.shaded.io.netty.channel.socket.nio.NioSocketChannel",
                "io.grpc.netty.shaded.io.netty.util.internal.NativeLibraryUtil",
                "io.grpc.netty.shaded.io.netty.util.ReferenceCountUtil",
                "io.grpc.netty.shaded.io.netty.buffer.AbstractByteBufAllocator",
                "io.grpc.netty.shaded.io.netty.channel.epoll.Epoll",
                "io.grpc.netty.shaded.io.netty.channel.epoll.EpollChannelOption",
                "io.grpc.netty.shaded.io.netty.channel.epoll.EpollEventLoopGroup",
                "io.grpc.netty.shaded.io.netty.channel.epoll.EpollServerSocketChannel",
                "io.grpc.netty.shaded.io.netty.channel.epoll.EpollSocketChannel",
                "io.grpc.internal.PickFirstLoadBalancerProvider",
                "io.grpc.protobuf.services.internal.HealthCheckingRoundRobinLoadBalancerProvider");
        reflections.produce(buildItem);

        String prefixPackageDir = "io.grpc.netty.shaded.io.netty.util.internal.shaded.org.jctools.queues";
        reflections.produce(new ReflectiveClassBuildItem(true, true, true,
                prefixPackageDir + ".MpscArrayQueueProducerIndexField",
                prefixPackageDir + ".MpscArrayQueueProducerLimitField",
                prefixPackageDir + ".MpscArrayQueueConsumerIndexField",
                prefixPackageDir + ".BaseMpscLinkedArrayQueueProducerFields",
                prefixPackageDir + ".BaseMpscLinkedArrayQueueColdProducerFields",
                prefixPackageDir + ".BaseMpscLinkedArrayQueueConsumerFields",
                "io.grpc.internal.DnsNameResolverProvider"
        ));

        Collection<ClassInfo> nrs = combinedIndex.getIndex().getAllKnownSubclasses(NAME_RESOLVER_PROVIDER);
        for (ClassInfo nr : nrs) {
            reflections.produce(new ReflectiveClassBuildItem(true, true, false, nr.name().toString()));
        }

        reflections.produce(new ReflectiveClassBuildItem(true, true, false, DnsNameResolverProvider.class));
        reflections.produce(new ReflectiveClassBuildItem(true, true, false, "io.grpc.util.SecretRoundRobinLoadBalancerProvider$Provider"));
        reflections.produce(new ReflectiveClassBuildItem(true, true, false, NettyChannelProvider.class));
        reflections.produce(new ReflectiveClassBuildItem(true, true, true, ReadableBuffers.class));
    }

    /**
     * The next step fails to package the project
     * /* [ERROR] [error]: Build step io.quarkus.deployment.steps.NativeImageResourcesStep#registerPackageResources threw an exception: java.lang.StringIndexOutOfBoundsException: begin 0, end -1, length 65
     */
    //    @BuildStep
    //    void addNativeResourceForNettyShaded(BuildProducer<NativeImageResourceDirectoryBuildItem> resourceBuildItem) {
    //        resourceBuildItem.produce(new NativeImageResourceDirectoryBuildItem("META-INF"));
    //    }
    @BuildStep
    void runTimeInitializationForNettyShaded(BuildProducer<RuntimeInitializedClassBuildItem> runtimeInitialized,
                                             BuildProducer<RuntimeInitializedPackageBuildItem> runtimePackages) {
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.OpenSsl"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.OpenSslContext"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.ReferenceCountedOpenSslEngine"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.JdkNpnApplicationProtocolNegotiator"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.JettyAlpnSslEngine"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.JettyAlpnSslEngine$ClientEngine"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.JettyAlpnSslEngine$ServerEngine"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.handler.ssl.JettyNpnSslEngine"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.internal.tcnative.SSL"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.internal.tcnative.CertificateVerifier"));
        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.netty.shaded.io.netty.internal.tcnative.SSLPrivateKeyMethod"));

        runtimePackages.produce(new RuntimeInitializedPackageBuildItem("io.grpc.netty.shaded.io.grpc.netty"));
        runtimePackages.produce(new RuntimeInitializedPackageBuildItem("io.grpc.netty.shaded.io.netty.channel.epoll"));
        runtimePackages.produce(new RuntimeInitializedPackageBuildItem("io.grpc.netty.shaded.io.netty.channel.unix"));

        runtimeInitialized.produce(new RuntimeInitializedClassBuildItem("io.grpc.internal.RetriableStream"));
    }

}
