package com.ainia.ecgApi.core.persistence;

import org.hibernate.event.spi.PostCollectionRemoveEvent;
import org.hibernate.event.spi.PostCollectionRemoveEventListener;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;

/**
 * <p>jpa(hibernate) 持久化监听器</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * PersistenceListener.java
 * @author pq
 * @createdDate 2013-8-13
 * @version
 */
public class PersistenceListener implements PostInsertEventListener,
		PostUpdateEventListener, PostDeleteEventListener,
		PostCollectionRemoveEventListener {

	@Override
	public void onPostRemoveCollection(PostCollectionRemoveEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPostDelete(PostDeleteEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		// TODO Auto-generated method stub

	}

}
