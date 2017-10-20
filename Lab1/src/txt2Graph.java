import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class txt2Graph {
	static class controlPanel extends JPanel {
		private class FileAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent ee) {
				// file chooser
				final JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jfc.showDialog(new JLabel(), "ѡ��");
				final File file = jfc.getSelectedFile();
				if (file.isDirectory()) {
					System.out.println("�ļ���:" + file.getAbsolutePath());
				} else if (file.isFile()) {
					System.out.println("�ļ�:" + file.getAbsolutePath());
				}
				System.out.println(jfc.getSelectedFile().getName());
				// read file
				fileName = file.getAbsolutePath();
				resultsFileName = fileName.substring(0, fileName.length() - 4) + "Results.txt";
				randomWalkFileName = resultsFileName.substring(0, resultsFileName.length() - 4) + "randomWalk" + "."
				        + "txt";
				// System.out.println("pathGraphFileName: "+pathGraphFileName);
				readFileToWords();
				readWordsToGraph();
				// graphFileName = graph.showGraph(resultsFileName);
			}

			public void readFileToWords() {
				final File file = new File(fileName);
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new FileReader(file));
					String tempString = null;
					while ((tempString = reader.readLine()) != null) {
						final String ss = tempString.replaceAll("[\\p{Punct}] + ", " "); // ����ɿո�
						System.out.println(ss);
						final String[] words = ss.trim().split("\\s+"); // ���ո�ָ�

						for (int i = 0; i < words.length; i++) { // ������ʽƥ����ĸ�����Сд

							final Pattern pp = Pattern.compile("a-z||A-Z");
							final Matcher mm = pp.matcher(words[i]);
							words[i] = mm.replaceAll("").trim().toLowerCase();
						}

						for (final String str : words) {
							write(fileName.substring(0, fileName.length() - 4) + "Results.txt", str + "\r\n");
						}
					}
					reader.close();
				} catch (final IOException e0) {
					e0.printStackTrace();
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (final IOException e1) {
							;
						}
					}
				}
			}

			public void readWordsToGraph() {
				graph = new DGraph();
				final File file = new File(resultsFileName);
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new FileReader(file));
					String tempString = null;
					String word1 = null;
					String word2 = null;
					while ((tempString = reader.readLine()) != null) {
						word2 = tempString;
						if (word1 != null) {
							graph.addEdge(word1, word2);
						}
						word1 = tempString;
						System.out.println(tempString);
					}
					reader.close();
				} catch (final IOException e0) {
					e0.printStackTrace();
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (final IOException e1) {
							;
						}
					}
				}
			}
		}

		private class NewTxtAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String sentence = JOptionPane.showInputDialog("sentence:");
				final String newText = graph.generateNewText(sentence);
				JOptionPane.showMessageDialog(null, newText, "new text", JOptionPane.INFORMATION_MESSAGE);
			}
		}

		private class RandomWalkAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String reslutsRandomWalk = graph.randomWalk();
				showPath(graph, reslutsRandomWalk);
				JOptionPane.showMessageDialog(null,
				        "\"" + reslutsRandomWalk + "\"" + " has been stored in " + randomWalkFileName, "random walk",
				        JOptionPane.INFORMATION_MESSAGE);
			}
		}

		private class ShortedtPathAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				String resultsShortestPath = null;
				String[] resultsShortestPathes;
				String word1 = null;
				String word2 = null;
				// JOptionPane.showInputDialog("word2:", JOptionPane.CANCEL_OPTION);
				word1 = JOptionPane.showInputDialog("word1:");
				word2 = JOptionPane.showInputDialog("word2:");
				System.out.println("word1: " + word1 + " ,word2: " + word2);
				if (word2 == null) {
					// ����ȡ������word2���ܽ���
					resultsShortestPathes = graph.calcShortestPath(word1);
					for (final String path : resultsShortestPathes) {
						final int res = JOptionPane.showConfirmDialog(null,
						        "next are the shortest path begin with word " + word1, "continue or not",
						        JOptionPane.YES_NO_OPTION);
						if (res == JOptionPane.NO_OPTION) {
							break;
						} else {
							final String[] srr = path.split("@");
							showPathes(graph, srr);
							final String[] p1 = srr[0].split(" ");
							word2 = p1[p1.length - 1];
							JOptionPane.showMessageDialog(null,
							        "From \"" + word1 + "\" to \"" + word2 + "\" there are " + srr.length
							                + " shortest path:   \r\n" + path,
							        "shortest path", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				} else if (word1 != null && word2 != null) {
					resultsShortestPath = graph.calcShortestPath(word1, word2);
					final String[] srr = resultsShortestPath.split("@");
					showPathes(graph, srr);
					JOptionPane.showMessageDialog(null,
					        "From \"" + word1 + "\" to \"" + word2 + "\" there are " + srr.length
					                + " shortest path:   \r\n" + resultsShortestPath,
					        "shortest path", JOptionPane.INFORMATION_MESSAGE);
				}

			}
		}

		private class ShowAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				// graph.showGraph(resultsFileName);
				showDirectedGraph(graph);
			}
		}

		private class WordBridgeAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {

				String word1 = null;
				String word2 = null;
				word1 = JOptionPane.showInputDialog("word1:");
				word2 = JOptionPane.showInputDialog("word2:");
				final String wordBridge = graph.queryBridgeWords(word1, word2);
				JOptionPane.showMessageDialog(null, wordBridge, "bridge word", JOptionPane.INFORMATION_MESSAGE);
				System.out.println(word1 + word2);
			}
		}

		private DGraph			graph;

		private final JPanel	panel;

		// all the buttons
		public controlPanel() {
			// setLayout(new BorderLayout());
			panel = new JPanel();
			addButton("open file", new FileAction());
			// fun2:show
			addButton("show", new ShowAction());

			// fun3:bridge word
			addButton("bridge word", new WordBridgeAction());

			// fun4:new text
			addButton("new text", new NewTxtAction());

			// fun5:shortest path
			addButton("shortest path", new ShortedtPathAction());

			// fun6:random walk
			addButton("random walk", new RandomWalkAction());

			// addButton("clear",new ClearAction());
			add(panel, BorderLayout.CENTER);
		}
		
		private void addButton(String label, ActionListener listener) {
			final JButton button = new JButton(label);
			button.addActionListener(listener);
			panel.add(button);
		}
	}
	// show directed graph

	static class translatorFrame extends JFrame {
		private static final int	DEFAULT_HEIGHT	= 600;

		private static final int	DEFAULT_WIDTH	= 800;
		
		public translatorFrame() {
			setTitle("�ı�����ͼ");
			setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
			final controlPanel control = new controlPanel();
			add(control, BorderLayout.SOUTH);
			label = new JLabel("");
			label.setLayout(new FlowLayout());
			add(label);
		}
	}
	// contro panel

	private static String			fileName			= null;

	// private static String graphFileName =
	// "C://Users//ibm//Desktop//testResults.gif";
	private static translatorFrame	frame;

	private static String			graphFileName		= null;

	private static ImageIcon		img;

	private static JLabel			label;

	private static String			pathGraphFileName	= null;
	private static String			randomWalkFileName	= null;
	private static String			resultsFileName		= null;
	
	// main function
	/**
	 * ������. duziyuan
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			frame = new translatorFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});
	}
	
	// show frame
	static void showDirectedGraph(DGraph graph) {
		graphFileName = graph.showGraph(resultsFileName);
		System.out.println(graphFileName);
		img = new ImageIcon(graphFileName);
		label.setIcon(img);
		label.setText(null);
	}
	
	static void showPath(DGraph graph, String path) {
		pathGraphFileName = graph.showGraphPath(resultsFileName, path);
		System.out.println("pathGraphFileName:" + pathGraphFileName);
		img = new ImageIcon(pathGraphFileName);
		label.setIcon(img);
		label.setText(null);
	}
	
	static void showPathes(DGraph graph, String[] pathes) {
		pathGraphFileName = graph.showGraphPathes(resultsFileName, pathes);
		System.out.println("pathGraphFileName:" + pathGraphFileName);
		img = new ImageIcon(pathGraphFileName);
		label.setIcon(img);
		label.setText(null);
	}
	
	static void write(String fileName, String str) {
		try {
			// new File(fileName);
			final FileWriter writer = new FileWriter(fileName, true);
			writer.write(str);
			;
			System.out.println("д���ļ��ɹ�");
			writer.close();
		} catch (final IOException e0) {
			e0.printStackTrace();
		}
	}
}
