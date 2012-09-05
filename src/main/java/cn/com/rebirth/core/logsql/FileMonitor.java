package cn.com.rebirth.core.logsql;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileMonitor {
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(FileMonitor.class);
	
	/** The Constant instance. */
	private static final FileMonitor instance = new FileMonitor();
	
	/** The timer. */
	private Timer timer;
	
	/** The timer entries. */
	private Map<String, FileMonitorTask> timerEntries;

	/**
	 * Instantiates a new file monitor.
	 */
	private FileMonitor() {
		this.timerEntries = new HashMap<String, FileMonitorTask>();
		this.timer = new Timer(true);
	}

	/**
	 * Gets the single instance of FileMonitor.
	 *
	 * @return single instance of FileMonitor
	 */
	public static FileMonitor getInstance() {
		return instance;
	}

	/**
	 * Adds the file change listener.
	 *
	 * @param listener the listener
	 * @param filename the filename
	 * @param period the period
	 */
	public void addFileChangeListener(FileChangeListener listener, String filename, long period) {
		this.removeFileChangeListener(filename);
		logger.info("Watching " + filename);
		FileMonitorTask task = new FileMonitorTask(listener, filename);
		this.timerEntries.put(filename, task);
		this.timer.schedule(task, period, period);
	}

	/**
	 * Removes the file change listener.
	 *
	 * @param filename the filename
	 */
	public void removeFileChangeListener(String filename) {
		FileMonitorTask task = this.timerEntries.remove(filename);
		if (task != null) {
			task.cancel();
		}
	}

	/**
	 * The Class FileMonitorTask.
	 *
	 * @author l.xue.nong
	 */
	private static class FileMonitorTask extends TimerTask {
		
		/** The listener. */
		private FileChangeListener listener;
		
		/** The filename. */
		private String filename;
		
		/** The monitored file. */
		private File monitoredFile;
		
		/** The last modified. */
		private long lastModified;

		/**
		 * Instantiates a new file monitor task.
		 *
		 * @param listener the listener
		 * @param filename the filename
		 */
		public FileMonitorTask(FileChangeListener listener, String filename) {
			this.listener = listener;
			this.filename = filename;
			this.monitoredFile = new File(filename);
			if (!this.monitoredFile.exists()) {
				return;
			}
			this.lastModified = this.monitoredFile.lastModified();
		}

		/* (non-Javadoc)
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
			long latestChange = this.monitoredFile.lastModified();
			if (this.lastModified != latestChange) {
				this.lastModified = latestChange;
				this.listener.fileChanged(this.filename);
			}
		}
	}
}
