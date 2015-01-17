package yk.paperpointer;

import yk.jcommon.fastgeom.Quaternionf;
import yk.jcommon.fastgeom.Vec3f;
import yk.jcommon.net.anio.AClient;
import yk.jcommon.net.anio.ASocket;
import yk.jcommon.net.anio.OnConnection;
import yk.jcommon.net.anio.OnData;
import yk.jcommon.utils.Threads;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: yuri
 * Date: 2/4/14
 * Time: 11:14 PM
 */
public class PaperClient {
    public Vec3f arrowCamSpacePos = Vec3f.ZERO;
    public Quaternionf arrowCamSpaceRot = Quaternionf.ZERO;
    private ASocket socket;
    private AClient client;

    public static void main(String[] args) {
        final PaperClient pc = new PaperClient();
        pc.connect();
        Threads.thread(new Threads.Foo() {
            @Override
            public boolean execute() throws Exception {
                pc.tick();
                return true;
            }
        });
    }

    public void connect() {
        //TODO block here for connection?
        socket = new ASocket("localhost", PaperServer.PORT, new OnConnection() {
            @Override
            public void call(AClient socket) {
                client = socket;
                client.onData = new OnData() {
                    @Override
                    public void call(Object data) {
                        arrowCamSpacePos = (Vec3f) ((List)data).get(0);
                        arrowCamSpaceRot = (Quaternionf) ((List)data).get(1);
                    }
                };
            }
            //TODO attempt to reconnect
        });
    }

    public void tick() {
        socket.tick();
    }

}
