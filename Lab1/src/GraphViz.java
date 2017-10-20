
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class GraphViz {
	private static String	dOT			= "G:\\graphviz\\bin\\dot.exe";
	private static String	TEMP_DIR	= "c:/temp";
	private StringBuilder	graph		= new StringBuilder();
	
	public GraphViz() {
	}
	
	public void add(String line) {
		graph.append(line);
	}
	
	public void addln() {
		graph.append('\n');
	}
	
	public void addln(String line) {
		graph.append(line + "\n");
	}
	
	public String end_graph() {
		return "}";
	}
	
	private byte[] get_img_stream(File dot, String type) {
		File img;
		byte[] imG = null;
		try {
			img = File.createTempFile("graph_", "." + type, new File(GraphViz.TEMP_DIR));
			final Runtime rt = Runtime.getRuntime();
			final String[] args = { dOT, "-T" + type, dot.getAbsolutePath(), "-o", img.getAbsolutePath() };
			final Process prO = rt.exec(args);
			prO.waitFor();
			final FileInputStream in = new FileInputStream(img.getAbsolutePath());
			imG = new byte[in.available()];
			in.read(imG);
			if (in != null) {
				in.close();
			}
			if (img.delete() == false) {
				System.err.println("Warning: " + img.getAbsolutePath() + " could not be deleted!");
			}
		} catch (final java.io.IOException ioe) {
			System.err.println("Error:    in I/O processing of tempfile in dir " + GraphViz.TEMP_DIR + "\n");
			System.err.println("       or in calling external command");
			ioe.printStackTrace();
		} catch (final java.lang.InterruptedException ie) {
			System.err.println("Error: the execution of the external program was interrup" + "ted");
			ie.printStackTrace();
		}
		return imG;
	}
	
	public String getDotSource() {
		return graph.toString();
	}
	
	/**
	 *
	 * @param type
	 *            leixing.
	 * @return fanhuizhi
	 */
	public byte[] getGraph(String doT, String type) {
		File dot;
		byte[] imG = null;
		try {
			dot = writeDotSourceToFile(doT);
			if (dot != null) {
				imG = get_img_stream(dot, type);
				if (dot.delete() == false) {
					System.err.println("Warning: " + dot.getAbsolutePath() + " could not be deleted!");
				}
				return imG;
			}
			return null;
		} catch (final java.io.IOException ioe) {
			return null;
		}
	}
	
	/**
	 *
	 * leixing. duziyuan
	 */
	
	public void readSource(String input) {
		final StringBuilder sb = new StringBuilder();
		try {
			final FileInputStream fis = new FileInputStream(input);
			final DataInputStream dis = new DataInputStream(fis);
			final BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			dis.close();
		} catch (final Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		graph = sb;
	}
	
	public String start_graph() {
		return "digraph G {";
	}
	
	private File writeDotSourceToFile(String str) throws java.io.IOException {
		File temp;
		try {
			temp = File.createTempFile("graph_", ".dot.tmp", new File(GraphViz.TEMP_DIR));
			final FileOutputStream fos = new FileOutputStream(temp.getAbsolutePath());
			final BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			br.write(str);
			br.close();
		} catch (final Exception e) {
			System.err.println("Error: I/O error while writing the dot source to temp " + "file!");
			return null;
		}
		return temp;
	}
	
	public int writeGraphToFile(byte[] img, File to) {
		try {
			final FileOutputStream fos = new FileOutputStream(to);
			fos.write(img);
			fos.close();
		} catch (final java.io.IOException ioe) {
			return -1;
		}
		return 1;
	}
	
	public int writeGraphToFile(byte[] img, String file) {
		final File to = new File(file);
		return writeGraphToFile(img, to);
	}
}