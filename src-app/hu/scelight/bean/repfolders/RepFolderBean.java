/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.bean.repfolders;

import hu.scelight.bean.adapter.PathAdapter;
import hu.scelight.bean.repfilters.RepFiltersBean;
import hu.sllauncher.bean.IdedBean;

import java.nio.file.Path;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Replay folder bean.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class RepFolderBean extends IdedBean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/** Origin of the replay folder. */
	private RepFolderOrigin origin;
	
	/** Replay folder path. */
	@XmlJavaTypeAdapter( value = PathAdapter.class )
	private Path            path;
	
	/** Tells if replays contained by sub-folders are also to be included. */
	private boolean         recursive;
	
	/** Tells if this replay folder is monitored for new replays. */
	private boolean         monitored;
	
	/** Comment for the replay folder. */
	private String          comment;
	
	/** Number of replays in this replay folder. */
	private Integer         replaysCount;
	
	/** Date when replays were counted. */
	private Date            countedAt;
	
	/** Replay filters of this replay folder. */
	private RepFiltersBean  repFiltersBean;
	
	
	/**
	 * Creates a new {@link RepFolderBean}.
	 */
	public RepFolderBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Returns the number of active filters.
	 * 
	 * @return the number of active filters
	 */
	public int getActiveFilterCount() {
		return repFiltersBean == null ? 0 : repFiltersBean.getActiveCount();
	}
	
	/**
	 * Tells if the monitored property is editable.
	 * 
	 * @return true if the monitored is editable
	 */
	public boolean isMonitoredEditable() {
		return origin != RepFolderOrigin.REP_BACKUP_FOLDER;
	}
	
	/**
	 * Tells if the path property is editable.
	 * 
	 * @return true if the path is editable
	 */
	public boolean isPathEditable() {
		return origin == RepFolderOrigin.USER;
	}
	
	/**
	 * Returns the origin of the replay folder.
	 * 
	 * @return the origin of the replay folder
	 */
	public RepFolderOrigin getOrigin() {
		return origin;
	}
	
	/**
	 * Sets the origin of the replay folder.
	 * 
	 * @param origin the origin of the replay folder to be set
	 */
	public void setOrigin( RepFolderOrigin origin ) {
		this.origin = origin;
	}
	
	/**
	 * Returns the replay folder path.
	 * 
	 * @return the replay folder path
	 */
	public Path getPath() {
		return path;
	}
	
	/**
	 * Sets the replay folder path.
	 * 
	 * @param path replay folder path to be set
	 */
	public void setPath( Path path ) {
		this.path = path;
	}
	
	/**
	 * Returns the recursive property.
	 * 
	 * @return true if this replay folder is recursive; false otherwise
	 */
	public boolean getRecursive() {
		return recursive;
	}
	
	/**
	 * Sets the recursive property.
	 * 
	 * @param recursive recursive property to be set
	 */
	public void setRecursive( boolean recursive ) {
		this.recursive = recursive;
	}
	
	/**
	 * Returns the monitored property.
	 * 
	 * @return true if this replay folder is monitored; false otherwise
	 */
	public boolean getMonitored() {
		return monitored;
	}
	
	/**
	 * Sets the monitored property.
	 * 
	 * @param monitored monitored property to be set
	 */
	public void setMonitored( boolean monitored ) {
		this.monitored = monitored;
	}
	
	/**
	 * Returns the comment for the replay folder.
	 * 
	 * @return the comment for the replay folder
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * Sets the comment for the replay folder.
	 * 
	 * @param comment comment for the replay folder to be set
	 */
	public void setComment( String comment ) {
		this.comment = comment;
	}
	
	/**
	 * Returns the number of replays in this replay folder.
	 * 
	 * @return the number of replays in this replay folder
	 */
	public Integer getReplaysCount() {
		return replaysCount;
	}
	
	/**
	 * Sets the number of replays in this replay folder.
	 * 
	 * @param replaysCount the number of replays in this replay folder to be set
	 */
	public void setReplaysCount( Integer replaysCount ) {
		this.replaysCount = replaysCount;
	}
	
	/**
	 * Returns the date when the replays were counted.
	 * 
	 * @return the date when repays were counted
	 */
	public Date getCountedAt() {
		return countedAt;
	}
	
	/**
	 * Sets the date when the replays were counted.
	 * 
	 * @param countedAt date to be set when replays were counted
	 */
	public void setCountedAt( Date countedAt ) {
		this.countedAt = countedAt;
	}
	
	/**
	 * Returns the replay filters of this replay folder.
	 * 
	 * @return the replay filters of this replay folder
	 */
	public RepFiltersBean getRepFiltersBean() {
		return repFiltersBean;
	}
	
	/**
	 * Sets the replay filters of this replay folder.
	 * 
	 * @param repFiltersBean replay filters to be set
	 */
	public void setRepFiltersBean( RepFiltersBean repFiltersBean ) {
		this.repFiltersBean = repFiltersBean;
	}
	
}
