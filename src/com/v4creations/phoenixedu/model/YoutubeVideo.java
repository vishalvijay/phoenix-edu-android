package com.v4creations.phoenixedu.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "youtube_videos")
public class YoutubeVideo {
	public static final String COL_YID = "_id";
	public static final String COL_TITLE = "title";
	public static final String COL_DESCRIPTION = "description";
	public static final String COL_CATEGORY = "category";
	public static final String COL_CHANNEL = "channel";
	public static final String COL_UPDATED_AT = "updated_at";
	public static final String COL_DURATION = "duration";
	public static final String COL_IS_WATCHED = "isWatched";
	public static final String COL_IS_FAVORITE = "isFavorite";
	@DatabaseField(index = true, id = true, columnName = "_id")
	private String yid;
	@DatabaseField
	private String title;
	@DatabaseField
	private String description;
	@DatabaseField
	private String category;
	@DatabaseField
	private String channel;
	@DatabaseField
	private String updated_at;
	@DatabaseField
	private long duration;
	@DatabaseField
	private boolean isWatched = false;
	@DatabaseField
	private boolean isFavorite = false;

	public YoutubeVideo() {
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getYid() {
		return yid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description == null)
			description = "";
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isWatched() {
		return isWatched;
	}

	public void setWatched(boolean isWatched) {
		this.isWatched = isWatched;
	}

	public String getUpdatedAt() {
		return updated_at;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updated_at = updatedAt;
	}

	public String toString() {
		return getUpdatedAt();
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
}
