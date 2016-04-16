package com.lon.smile.framework;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.lon.smile.framework.Shard.Node;

public class Shard<Node> { // S���װ�˻����ڵ����Ϣ ����name��password��ip��port��

	static private TreeMap<Long, Node> nodes; // ����ڵ㵽��ʵ�ڵ��ӳ��
	static private TreeMap<Long, Node> treeKey; // key����ʵ�ڵ��ӳ��
	static private List<Node> shards = new ArrayList<Node>(); // ��ʵ�����ڵ�
	private final int NODE_NUM = 100; // ÿ�������ڵ����������ڵ����
	boolean flag = false;

	public Shard(List<Node> shards) {
		super();
		this.shards = shards;
		init();
	}

	public static void main(String[] args) {
		// System.out.println(hash("w222o1d"));
		// System.out.println(Long.MIN_VALUE);
		// System.out.println(Long.MAX_VALUE);
		Node s1 = new Node("s1", "192.168.1.1");
		Node s2 = new Node("s2", "192.168.1.2");
		Node s3 = new Node("s3", "192.168.1.3");
		Node s4 = new Node("s4", "192.168.1.4");
		Node s5 = new Node("s5", "192.168.1.5");
		shards.add(s1);
		shards.add(s2);
		shards.add(s3);
		shards.add(s4);
		Shard<Node> sh = new Shard<Shard.Node>(shards);
		System.out.println("��ӿͻ��ˣ�һ��ʼ��4���������ֱ�Ϊs1,s2,s3,s4,ÿ��������100������������");
		sh.keyToNode("101�ͻ���");
		sh.keyToNode("102�ͻ���");
		sh.keyToNode("103�ͻ���");
		sh.keyToNode("104�ͻ���");
		sh.keyToNode("105�ͻ���");
		sh.keyToNode("106�ͻ���");
		sh.keyToNode("107�ͻ���");
		sh.keyToNode("108�ͻ���");
		sh.keyToNode("109�ͻ���");
//
//		sh.deleteS(s2);
//
//		sh.addS(s5);

		System.out.println("���Ŀͻ��˵�������ӳ��Ϊ��");
		printKeyTree();
	}

	public static void printKeyTree() {
		for (Iterator<Long> it = treeKey.keySet().iterator(); it.hasNext();) {
			Long lo = it.next();
			System.out.println("hash(" + lo + ")���ӵ�����->" + treeKey.get(lo));
		}

	}

	private void init() { // ��ʼ��һ����hash��
		nodes = new TreeMap<Long, Node>();
		treeKey = new TreeMap<Long, Node>();
		for (int i = 0; i != shards.size(); ++i) { // ÿ����ʵ�����ڵ㶼��Ҫ��������ڵ�
			final Node shardInfo = shards.get(i);
			for (int n = 0; n < NODE_NUM; n++)
				// һ����ʵ�����ڵ����NODE_NUM������ڵ�
				nodes.put(hash("SHARD-" + shardInfo.name + "-NODE-" + n), shardInfo);
		}
	}

	// ����һ������
	private void addS(Node s) {
		System.out.println("��������" + s + "�ı仯��");
		for (int n = 0; n < NODE_NUM; n++)
			addS(hash("SHARD-" + s.name + "-NODE-" + n), s);

	}

	// ���һ������ڵ�����νṹ,lgΪ����ڵ��hashֵ
	public void addS(Long lg, Node s) {
		SortedMap<Long, Node> tail = nodes.tailMap(lg);
		SortedMap<Long, Node> head = nodes.headMap(lg);
		Long begin = 0L;
		Long end = 0L;
		SortedMap<Long, Node> between;
		if (head.size() == 0) {
			between = treeKey.tailMap(nodes.lastKey());
			flag = true;
		} else {
			begin = head.lastKey();
			between = treeKey.subMap(begin, lg);
			flag = false;
		}
		nodes.put(lg, s);
		for (Iterator<Long> it = between.keySet().iterator(); it.hasNext();) {
			Long lo = it.next();
			if (flag) {
				treeKey.put(lo, nodes.get(lg));
				System.out.println("hash(" + lo + ")�ı䵽->" + tail.get(tail.firstKey()));
			} else {
				treeKey.put(lo, nodes.get(lg));
				System.out.println("hash(" + lo + ")�ı䵽->" + tail.get(tail.firstKey()));
			}
		}
	}

	// ɾ����ʵ�ڵ���s
	public void deleteS(Node s) {
		if (s == null) {
			return;
		}
		System.out.println("ɾ������" + s + "�ı仯��");
		for (int i = 0; i < NODE_NUM; i++) {
			// ��λs�ڵ�ĵ�i������ڵ��λ��
			SortedMap<Long, Node> tail = nodes.tailMap(hash("SHARD-" + s.name + "-NODE-" + i));
			SortedMap<Long, Node> head = nodes.headMap(hash("SHARD-" + s.name + "-NODE-" + i));
			Long begin = 0L;
			Long end = 0L;

			SortedMap<Long, Node> between;
			if (head.size() == 0) {
				between = treeKey.tailMap(nodes.lastKey());
				end = tail.firstKey();
				tail.remove(tail.firstKey());
				nodes.remove(tail.firstKey());// ��nodes��ɾ��s�ڵ�ĵ�i������ڵ�
				flag = true;
			} else {
				begin = head.lastKey();
				end = tail.firstKey();
				tail.remove(tail.firstKey());
				between = treeKey.subMap(begin, end);// ��s�ڵ�ĵ�i������ڵ������key�ļ���
				flag = false;
			}
			for (Iterator<Long> it = between.keySet().iterator(); it.hasNext();) {
				Long lo = it.next();
				if (flag) {
					treeKey.put(lo, tail.get(tail.firstKey()));
					System.out.println("hash(" + lo + ")�ı䵽->" + tail.get(tail.firstKey()));
				} else {
					treeKey.put(lo, tail.get(tail.firstKey()));
					System.out.println("hash(" + lo + ")�ı䵽->" + tail.get(tail.firstKey()));
				}
			}
		}

	}

	// ӳ��key����ʵ�ڵ�
	public void keyToNode(String key) {
		SortedMap<Long, Node> tail = nodes.tailMap(hash(key)); // �ػ���˳ʱ���ҵ�һ������ڵ�
		if (tail.size() == 0) {
			return;
		}
		treeKey.put(hash(key), tail.get(tail.firstKey()));
		System.out.println(key + "��hash��" + hash(key) + "�����ӵ�����->" + tail.get(tail.firstKey()));
	}

	/**
	 * MurMurHash�㷨���ǷǼ���HASH�㷨�����ܸܺߣ�
	 * �ȴ�ͳ��CRC32,MD5��SHA-1���������㷨���Ǽ���HASH�㷨�����Ӷȱ���ͺܸߣ������������ϵ���Ҳ���ɱ��⣩
	 * ��HASH�㷨Ҫ��ܶ࣬���Ҿ�˵����㷨����ײ�ʺܵ�. http://murmurhash.googlepages.com/
	 */
	private static Long hash(String key) {

		ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
		int seed = 0x1234ABCD;

		ByteOrder byteOrder = buf.order();
		buf.order(ByteOrder.LITTLE_ENDIAN);

		long m = 0xc6a4a7935bd1e995L;
		int r = 47;

		long h = seed ^ (buf.remaining() * m);

		long k;
		while (buf.remaining() >= 8) {
			k = buf.getLong();

			k *= m;
			k ^= k >>> r;
			k *= m;

			h ^= k;
			h *= m;
		}

		if (buf.remaining() > 0) {
			ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
			// for big-endian version, do this first:
			// finish.position(8-buf.remaining());
			finish.put(buf).rewind();
			h ^= finish.getLong();
			h *= m;
		}

		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;

		buf.order(byteOrder);
		return h;
	}

	static class Node {
		String name;
		String ip;

		public Node(String name, String ip) {
			this.name = name;
			this.ip = ip;
		}

		@Override
		public String toString() {
			return this.name + "-" + this.ip;
		}
	}

}