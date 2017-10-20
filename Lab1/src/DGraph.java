
//import java.awt.Color;
import java.io.File;
import java.util.Random;

//import java.util.regex.*;

//import java.util.regex.Matcher;

/**
 *
 *
 * @author
 */
public class DGraph {
	
	public static final int		MAX		= 32767;
	
	private final LinkedList[]	adj		= new LinkedList[MAX];
	
	private int					version	= 1;
	
	private int					vertex;							// num of vertex
	
	public DGraph() {
		vertex = 0;
	}
	
	/**
	 *
	 * leixing. duziyuan
	 */
	
	public void addEdge(String word1, String word2) {
		int index1 = -1;
		int index2 = -1;
		// �ҵ�����1��Ӧ�Ľڵ㣬���ߴ���һ��
		for (int i = 0; i < vertex; i++) {
			if (adj[i].getHead().word.equals(word1)) {
				index1 = i;
			}
		}
		if (-1 == index1) {
			adj[vertex] = new LinkedList(word1);
			index1 = vertex;
			vertex++;
		}
		// �ҵ�����2��Ӧ�Ľڵ㣬���ߴ���һ��
		for (int i = 0; i < vertex; i++) {
			if (adj[i].getHead().word.equals(word2)) {
				index2 = i;
			}
		}
		if (-1 == index2) {
			adj[vertex] = new LinkedList(word2);
			index2 = vertex;
			vertex++;
		}
		for (Node node = adj[index1].getHead().next; node != null; node = node.next) {
			if (node.word.equals(word2)) {
				node.weight++;
				return;
			}
		}
		adj[index1].addNode(word2, index2);
	}
	
	/**
	 * ���·��. duziyuan
	 */
	public String[] calcShortestPath(String word1) { // ���·����Dijkstra�㷨,ֻ��һ������
		final int[] path = new int[vertex]; // ��¼�����ĵ�
		final boolean[] visited = new boolean[vertex]; // ����Ƿ���ʹ�
		final int[] dist = new int[vertex];
		int index1 = -1; // word1������
		// int index2 = -1; //word2������
		final String[] reply = new String[vertex - 1];
		for (int i = 0; i < vertex; i++) {
			if (adj[i].getHead().word.equals(word1)) { // �ҵ���word1Ϊ����ı�
				index1 = i;
			}
			dist[i] = MAX;
			visited[i] = false;
			
		}
		if (index1 == -1) {
			reply[0] = "���ʲ����ڣ�";
			return reply;
		}
		
		visited[index1] = true;
		dist[index1] = 0;
		Node verte = adj[index1].getHead().next; // word1�����ӵĵ�
		while (verte != null) {
			dist[verte.num] = verte.weight;
			path[verte.num] = index1;
			verte = verte.next;
		}
		for (int i = 1; i < vertex; i++) { // z���ѭ��(V-1)��
			
			int mindist = MAX; // ��ǰ�����·��
			int interVertex = index1; // ;�����м��
			for (int j = 0; j < vertex; j++) {
				if (!visited[j] && dist[j] < mindist) {
					mindist = dist[j];
					interVertex = j;
				}
			}
			visited[interVertex] = true;
			
			Node interNode = adj[interVertex].getHead().next;
			while (interNode != null) { // ����dist
				if (!visited[interNode.num] && dist[interNode.num] > dist[interVertex] + interNode.weight) {
					dist[interNode.num] = dist[interVertex] + interNode.weight;
					path[interNode.num] = interVertex;
				}
				interNode = interNode.next;
			}
		}
		final String[] wordPath = new String[vertex];
		int num = 0;
		for (int i = 0; i < vertex; i++) {
			if (visited[i] == false) {
				reply[num] = adj[i].getHead().word + " ���ɴ";
				num++;
			}
			
		}
		for (int i = 0; i < vertex; i++) { // �������������·��
			
			if (i != index1 && visited[i] == true) {
				wordPath[0] = adj[i].getHead().word;
				int pathIndex = i;
				int pathNum = 1;
				while (path[pathIndex] != index1) { // ��;���ĵ��ʼ��뵽WordPath�У�Ϊ����
					pathIndex = path[pathIndex];
					wordPath[pathNum] = adj[pathIndex].getHead().word;
					pathNum++;
				}
				final StringBuilder builder = new StringBuilder();
				builder.append(word1);
				for (int k = pathNum - 1; k >= 0; k--) {
					builder.append(" " + wordPath[k]);
				}
				reply[num] = builder.toString();
				num++;
			}
		}
		
		return reply;
	}
	
	/**
	 * չʾͼ��·��. duziyuan
	 */
	// ���·����Dijkstra�㷨�Ľ�����������·��
	public String calcShortestPath(String word1, String word2) {
		final int[][] path = new int[vertex][vertex]; // ��¼һ�����·������һ�ڵ�
		final boolean[] visited = new boolean[vertex]; // ����Ƿ���ʹ�
		final int[] dist = new int[vertex];
		int index1 = -1; // word1������
		int index2 = -1; // word2������
		String reply = new String();
		boolean flag = false; // �ж�word2�Ƿ�ɴ�
		for (int i = 0; i < vertex; i++) {
			if (adj[i].getHead().word.equals(word1)) { // �ҵ���word1Ϊ����ı�
				index1 = i;
			}
			if (adj[i].getHead().word.equals(word2)) { // �ҵ���word2Ϊ����ı�
				index2 = i;
			}
			dist[i] = MAX;
			visited[i] = false;
			for (int j = 0; j < vertex; j++) {
				path[i][j] = -1;
			}
		}
		
		if (index1 == -1 || index2 == -1) {
			return "���ʲ����ڣ�";
		}
		visited[index1] = true;
		dist[index1] = 0;
		Node vert = adj[index1].getHead().next; // word1�����ӵĵ�
		while (vert != null) {
			dist[vert.num] = vert.weight;
			path[vert.num][0] = index1; // ���ܻ��м���·��
			vert = vert.next;
		}
		for (int i = 1; i < vertex; i++) { // z���ѭ��(V-1)��
			int mindist = MAX; // ��ǰ�����·��
			for (int j = 0; j < vertex; j++) {
				if (!visited[j] && dist[j] < mindist) {
					mindist = dist[j];
				}
			}
			final int[] interVertexs = new int[vertex];
			int interVertexNum = 0;
			for (int j = 0; j < vertex; j++) {
				if (!visited[j] && dist[j] == mindist) {
					interVertexs[interVertexNum] = j;
					visited[j] = true;
					interVertexNum++;
				}
			}
			if (visited[index2]) { // �ж��Ƿ���word2
				flag = true;
				break;
			}
			for (int k = 0; k < vertex; k++) {
				if (visited[k]) { // ����δ���ʹ��Ľڵ㣬�ж����·��������Щ�ѷ��ʹ��ĵ�
					Node notVisited = adj[k].getHead().next;
					while (notVisited != null) {
						if (!visited[notVisited.num]) { // δ���ʹ��ĵ���Ը���dist
							if (dist[notVisited.num] > dist[k] + notVisited.weight) { // ��Ҫ����
								dist[notVisited.num] = dist[k] + notVisited.weight;
								// �ı�path
								path[notVisited.num][0] = k;
								for (int z = 1; z < vertex; z++) {
									if (path[notVisited.num][z] >= 0) {
										path[notVisited.num][z] = -1;
									} else {
										break;
									}
								}
							} else if (dist[notVisited.num] == dist[k] + notVisited.weight) { // ��Ҫ�����µ�path
								for (int z = 0; z < vertex; z++) {
									if (path[notVisited.num][z] == -1) {
										path[notVisited.num][z] = k;
										break;
									}
								}
							}
						}
						notVisited = notVisited.next;
					}
				}
			}
			
		}
		if (flag == false) {
			reply = "���ɴ";
			return reply;
		}
		reply = displayPath(index1, index2, path);
		final String[] wordSplit = reply.split("@");
		final StringBuilder replyBuilder = new StringBuilder();
		
		for (int j = 0; j < wordSplit.length; j++) {
			wordSplit[j] = wordSplit[j] + " " + word2;
			replyBuilder.append(wordSplit[j] + "@");
		}
		return replyBuilder.toString();
	}
	
	/**
	 * չʾ·��. duziyuan
	 */
	public String displayPath(int start, int end, int[][] path) {
		// ��������·��ʱ���·�� �м�@��������õݹ飬����
		// startΪ��ʼ��Դ�㣬endΪ�յ㣬pathΪ��ά����
		if (start == end) {
			return adj[start].getHead().word;
		}
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < vertex; i++) {
			if (path[end][i] != -1) {
				final StringBuilder wordbuilder = new StringBuilder();
				final String midString = displayPath(start, path[end][i], path);
				final String[] pathWords = midString.split("@"); // ��@�ֿ����洢��ʱ��ÿ��·����@�ָ�
				for (int j = 0; j < pathWords.length; j++) {
					if (path[end][i] != start) {
						pathWords[j] = pathWords[j] + " " + adj[path[end][i]].getHead().word;
					}
					
					wordbuilder.append(pathWords[j] + "@");
				}
				builder.append(wordbuilder.toString());
			}
		}
		return builder.toString();
	}
	
	/**
	 * һ�����ļ�. duziyuan
	 */
	public String generateNewText(String inputText) {
		final String st = inputText.replaceAll("[\\p{Punct}\\p{Space}]+", " "); // ����ɿո�
		final String[] words = st.trim().split("\\s+"); // ���ո�ָ�
		if (words.length <= 2) {
			return "the input text should be longer";
		}
		String text = words[0];
		String word1;
		String word2;
		String word3;
		for (int index = 0; index < words.length - 1; index++) {
			word1 = words[index].toLowerCase();
			word2 = words[index + 1].toLowerCase();
			word3 = oneBridgeWord(word1, word2);
			if (word3 == null) {
				text = text + " " + word2;
			} else {
				text = text + " " + word3 + " " + word2;
			}
		}
		return text;
	}
	
	/**
	 *
	 * һ���ŽӴ�. duziyuan
	 */
	public String oneBridgeWord(String word1, String word2) {
		int index1 = -1;
		int index2 = -1;
		int index3;
		Node node3;
		Node node4;
		for (int i = 0; i < vertex; i++) {
			if (word1.equals(adj[i].getHead().word)) {
				index1 = i;
			}
			if (word1.equals(adj[i].getHead().word)) {
				index2 = i;
			}
		}
		if (index1 == -1 || index2 == -1) {
			return null;
		}
		node3 = adj[index1].getHead().next;
		while (node3 != null) {
			index3 = node3.num;
			node4 = adj[index3].getHead().next;
			while (node4 != null) {
				if (node4.word.equals(word2)) {
					return node3.word;
				}
				node4 = node4.next;
			}
			node3 = node3.next;
		}
		return null;
	}
	
	/**
	 *
	 * �ŽӴ�. duziyuan
	 */
	
	public String queryBridgeWords(String word1, String word2) {
		int index1 = -1;
		int index2 = -1;
		int index3;
		Node node3;
		Node node4;
		for (int i = 0; i < vertex; i++) {
			if (word1.equals(adj[i].getHead().word)) {
				index1 = i;
			}
			if (word1.equals(adj[i].getHead().word)) {
				index2 = i;
			}
		}
		if (index1 == -1 || index2 == -1) {
			return "No \"" + word1 + "\" or \"" + word2 + "\" in the graph!";
		}
		String words = "";
		node3 = adj[index1].getHead().next;
		while (node3 != null) {
			index3 = node3.num;
			node4 = adj[index3].getHead().next;
			while (node4 != null) {
				if (node4.word.equals(word2)) {
					words = words + node3.word + ",";
					break;
				}
				node4 = node4.next;
			}
			node3 = node3.next;
		}
		if (words.equals("")) {
			return "No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!";
		} else {
			return "The bridge words from \"" + word1 + "\" to \"" + word2 + "\" are:" + words;
		}
	}
	
	/**
	 * �������. duziyuan
	 */
	public String randomWalk() { // �������,���ѡ��ڵ㿪ʼ����
		final int vertexNum = vertex; // �ڵ����
		String reply = new String();
		int maxEdgeNum = 0; // ����������
		for (int i = 0; i < vertexNum; i++) {
			if (adj[i].nodeNum > maxEdgeNum) {
				maxEdgeNum = adj[i].nodeNum;
			}
		}
		final Random ra = new Random();
		final int[][] walkVisited = new int[vertexNum][maxEdgeNum]; // ����߹��ı�
		for (int i = 0; i < vertexNum; i++) {
			for (int j = 0; j < maxEdgeNum; j++) {
				walkVisited[i][j] = -1;
			}
		}
		int walkVertex = -1; // ���������ʼ�Ľڵ�����
		while (walkVertex < 0) {
			walkVertex = ra.nextInt() % vertexNum;
		}
		// System.out.println(WalkVertex);
		final StringBuilder wordBuilder = new StringBuilder();
		final String firstword = adj[walkVertex].getHead().word;
		wordBuilder.append(firstword);
		while (true) {
			int next = -1; // ����ı�
			if (adj[walkVertex].nodeNum == 0) {
				break; // ����ĳ���Ϊ0
			}
			while (next < 0) {
				next = ra.nextInt() % adj[walkVertex].nodeNum;
			}
			Node nextNode = adj[walkVertex].getHead().next;
			for (int j = 0; j < next; j++) {
				nextNode = nextNode.next;
			}
			final int nextVertex = nextNode.num; // ���ߵ���һ����
			wordBuilder.append(" " + adj[nextVertex].getHead().word);
			boolean flag = false;
			int edgeNum = 0;
			for (int j = 0; j < adj[walkVertex].nodeNum; j++) { // ���ұ��Ƿ����߹�
				if (walkVisited[walkVertex][j] == -1) {
					edgeNum = j;
					break;
				} else if (walkVisited[walkVertex][j] == nextVertex) {
					flag = true;
					break;
				}
			}
			if (flag) { // ������ʹ��Ƴ�
				break;
			} else { // �������Ѱ��
				walkVisited[walkVertex][edgeNum] = nextVertex;
				walkVertex = nextVertex;
			}
		}
		// WordBuilder.append(" "+adj[nextVertex].first.Word);
		reply = wordBuilder.toString();
		return reply;
	}

	/**
	 * չʾͼ. duziyuan
	 */
	public String showGraph(String resultsFileName) {
		final GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		for (int i = 0; i < vertex; i++) {
			final Node head = adj[i].getHead();
			Node tail = adj[i].getHead().next;
			while (null != tail) {
				gv.addln(head.word + "->" + tail.word + "[label=" + tail.weight + "]");
				tail = tail.next;
			}
		}
		// gv.addln("to->seek->out");
		// gv.addln("to->seek->out[color=RED]");
		gv.addln(gv.end_graph());
		System.out.println(gv.getDotSource());
		
		final String type = "gif";
		final String graphFileName = resultsFileName.substring(0, resultsFileName.length() - 4) + "." + type;
		final File out = new File(graphFileName);
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
		return graphFileName;
	}

	/**
	 * չʾͼ��. duziyuan
	 */
	
	public String showGraphPath(String resultsFileName, String path) {
		final String[] srr = path.split(" ");
		final GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		for (int i = 0; i < vertex; i++) {
			final Node head = adj[i].getHead();
			Node tail = adj[i].getHead().next;
			while (null != tail) {
				gv.addln(head.word + "->" + tail.word + "[label=" + tail.weight + "]");
				tail = tail.next;
			}
		}
		String temp1 = null;
		String temp2 = null;
		for (final String s : srr) {
			temp1 = temp2;
			temp2 = s;
			if (temp1 != null) {
				gv.addln(temp1 + "->" + temp2 + "[color=red]");
			}
			
		}
		// gv.addln("to->seek->out");
		// gv.addln("to->seek->out[color=RED]");
		gv.addln(gv.end_graph());
		System.out.println(gv.getDotSource());
		
		final String type = "gif";
		
		final String pathGraphFileName = resultsFileName.substring(0, resultsFileName.length() - 4) + "Path" + version
		        + "." + type;
		final File out = new File(pathGraphFileName);
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
		version++;
		return pathGraphFileName;
	}
	
	/**
	 * չʾͼ��·��. duziyuan
	 */
	public String showGraphPathes(String resultsFileName, String[] pathes) {
		final GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		for (int i = 0; i < vertex; i++) {
			final Node head = adj[i].getHead();
			Node tail = adj[i].getHead().next;
			while (null != tail) {
				gv.addln(head.word + "->" + tail.word + "[label=" + tail.weight + "]");
				tail = tail.next;
			}
		}
		// private RandomGenerator rgen = new RandomGenerator();
		final String[] color = { "red", "orange", "blue", "yellow", "green", "pink" };
		final Random rand = new Random();
		for (final String path : pathes) {
			// Color color =
			final String[] srr = path.split(" ");
			String temp1 = null;
			String temp2 = null;
			final String co = color[rand.nextInt(5)];
			for (final String s : srr) {
				temp1 = temp2;
				temp2 = s;
				if (temp1 != null) {
					gv.addln(temp1 + "->" + temp2 + "[color=" + co + "]");
				}
			}
		}
		
		gv.addln(gv.end_graph());
		System.out.println(gv.getDotSource());
		
		final String type = "gif";
		
		final String pathGraphFileName = resultsFileName.substring(0, resultsFileName.length() - 4) + "Path" + version
		        + "." + type;
		final File out = new File(pathGraphFileName);
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
		version++;
		return pathGraphFileName;
	}
}

class LinkedList {
	private Node	head	= null;
	public int		nodeNum;
	private Node	tail	= null;
	
	public LinkedList() {
		nodeNum = -1;
	}
	
	public LinkedList(String ww) {
		nodeNum = 0;
		final Node newNode = new Node(ww, -1);
		head = newNode;
		tail = newNode;
	}
	
	public void addNode(String ww, int nn) {
		final Node newNode = new Node(ww, nn);
		if (isEmpty()) {
			head = tail = newNode;
		} else {
			tail.next = newNode;
			tail = newNode;
		}
		nodeNum++;
	}
	
	public Node getHead() {
		return head;
	}
	
	public Node getTail() {
		return tail;
	}
	
	public boolean isEmpty() {
		
		return head == null;
	}
}

class Node {
	public Node		next;
	public int		num;
	public boolean	painted;
	public int		weight;
	public String	word;
	
	public Node() {
		word = null;
		next = null;
		num = weight = 0;
	}
	
	public Node(String ww, int nn) {
		word = ww;
		weight = 1;
		next = null;
		num = nn;
	}
}