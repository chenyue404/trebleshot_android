/*
 * Copyright (C) 2019 Veli Tasalı
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.genonbeta.TrebleShot.fragment;

import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.genonbeta.TrebleShot.R;
import com.genonbeta.TrebleShot.adapter.VideoListAdapter;
import com.genonbeta.TrebleShot.app.GalleryGroupEditableListFragment;
import com.genonbeta.TrebleShot.util.AppUtils;
import com.genonbeta.TrebleShot.widget.GroupEditableListAdapter;

public class VideoListFragment extends GalleryGroupEditableListFragment<VideoListAdapter.VideoHolder,
        GroupEditableListAdapter.GroupViewHolder, VideoListAdapter>
{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setFilteringSupported(true);
        setDefaultOrderingCriteria(VideoListAdapter.MODE_SORT_ORDER_DESCENDING);
        setDefaultSortingCriteria(VideoListAdapter.MODE_SORT_BY_DATE);
        setDefaultViewingGridSize(3, 5);
        setUseDefaultPaddingDecoration(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        setEmptyImage(R.drawable.ic_video_library_white_24dp);
        setEmptyText(getString(R.string.text_listEmptyVideo));
    }

    @Override
    public void onResume()
    {
        super.onResume();

        getContext().getContentResolver().registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                true, getDefaultContentObserver());
    }

    @Override
    public void onPause()
    {
        super.onPause();

        getContext().getContentResolver().unregisterContentObserver(getDefaultContentObserver());
    }

    @Override
    public VideoListAdapter onAdapter()
    {
        final AppUtils.QuickActions<GroupEditableListAdapter.GroupViewHolder> quickActions = clazz -> {
            if (!clazz.isRepresentative()) {
                registerLayoutViewClicks(clazz);

                View visitView = clazz.getView().findViewById(R.id.visitView);
                visitView.setOnClickListener(
                        v -> performLayoutClickOpen(clazz));

                visitView.setOnLongClickListener(v -> performLayoutLongClick(clazz));

                clazz.getView().findViewById(getAdapter().isGridLayoutRequested()
                        ? R.id.selectorContainer : R.id.selector)
                        .setOnClickListener(v -> {
                            if (getEngineConnection() != null)
                                getEngineConnection().setSelected(clazz.getAdapterPosition());
                        });
            }
        };

        return new VideoListAdapter(getActivity())
        {
            @NonNull
            @Override
            public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                return AppUtils.quickAction(super.onCreateViewHolder(parent, viewType), quickActions);
            }
        };
    }

    @Override
    public boolean onDefaultClickAction(GroupEditableListAdapter.GroupViewHolder holder)
    {
        return getEngineConnection() != null
                ? getEngineConnection().setSelected(holder)
                : performLayoutClickOpen(holder);
    }

    @Override
    public int onGridSpanSize(int viewType, int currentSpanSize)
    {
        return viewType == VideoListAdapter.VIEW_TYPE_TITLE ? currentSpanSize
                : super.onGridSpanSize(viewType, currentSpanSize);
    }

    @Override
    public CharSequence getDistinctiveTitle(Context context)
    {
        return context.getString(R.string.text_video);
    }
}
