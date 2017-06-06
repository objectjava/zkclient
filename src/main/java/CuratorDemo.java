import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

/**
 * CuratorDemo
 *
 * @author Song Yang
 * @date 2017/6/6
 */
public class CuratorDemo {
    private CuratorFramework client=null;
    public CuratorDemo(){
        RetryPolicy retryPolicy=new ExponentialBackoffRetry(1000,3);
        client= CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                .namespace("base").build();
        client.start();
    }
    public void closeClient(){
        if(client!=null){
            this.client.close();
        }
    }
    public void createNode(String path,byte[] data) throws Exception {
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(path,data);
    }
    public void deleteNode(String path,int version) throws Exception{
        client.delete().guaranteed().deletingChildrenIfNeeded().withVersion(version)
                .inBackground(new DeleteCallBack()).forPath(path); }

    public void readNode(String path) throws Exception {
        Stat stat = new Stat();
        byte[] data = client.getData().storingStatIn(stat).forPath(path);
        System.out.println("读取节点" + path + "的数据:" + new String(data));
        System.out.println(stat.toString());
    }
    public void updateNode(String path, byte[] data, int version)
            throws Exception {
        client.setData().withVersion(version).forPath(path, data);
    }
    public void getChildren(String path) throws Exception {
//        List<String> children = client.getChildren().usingWatcher().forPath("/curator");
       /* for (String pth : children) {
            System.out.println("child=" + pth);
        }*/
    }
    public static void main(String[] args){
    }
}
