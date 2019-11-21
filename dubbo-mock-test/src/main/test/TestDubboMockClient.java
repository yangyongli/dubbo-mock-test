import com.alibaba.dubbo.common.URL;
import com.dubbo.mock.test.client.DubboMockClient;
import org.apache.dubbo.remoting.RemotingException;
import org.junit.Before;
import org.junit.Test;

public class TestDubboMockClient {

    private URL url = URL.valueOf("dubbo://47.97.46.201:20188/com.ydzb.chn.router.request.PaymentRequest?anyhost=false" +
            "&application=financial-service-provider&bean.name=com.ydzb.chn.router.request.PaymentRequest" +
            "&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&" +
            "interface=com.ydzb.chn.router.request.PaymentRequest&logger=slf4j" +
            "&methods=payment" +
            "&pid=16536&register=true&release=2.7.3&retries=0&revision=1.0-SNAPSHOT&side=provider" +
            "&timeout=30000&timestamp=1573193597933");
    private DubboMockClient client;

    @Before
    public void testConnect() throws RemotingException {

        client = new DubboMockClient(url);
        client.doConnect();
    }

    @Test
    public void testSend() throws Exception {


    }

}