/*
 * Copyright (C) 2020 Veli Tasalı
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

package com.genonbeta.TrebleShot.ui.callback;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.genonbeta.TrebleShot.R;
import com.genonbeta.TrebleShot.app.Activity;
import com.genonbeta.TrebleShot.app.EditableListFragment;
import com.genonbeta.TrebleShot.app.EditableListFragmentImpl;
import com.genonbeta.TrebleShot.object.Shareable;
import com.genonbeta.android.framework.ui.PerformerMenu;
import com.genonbeta.android.framework.util.actionperformer.PerformerEngineProvider;

public class SharingPerformerMenuCallback extends EditableListFragment.SelectionCallback<Shareable>
{
    public SharingPerformerMenuCallback(Activity activity, PerformerEngineProvider provider)
    {
        super(activity, provider);
    }

    @Override
    public boolean onPerformerMenuList(PerformerMenu performerMenu, MenuInflater inflater, Menu targetMenu)
    {
        super.onPerformerMenuList(performerMenu, inflater, targetMenu);
        inflater.inflate(R.menu.action_mode_share, targetMenu);
        return true;
    }

    @Override
    public boolean onPerformerMenuClick(PerformerMenu performerMenu, MenuItem item)
    {
        return super.onPerformerMenuClick(performerMenu, item);
    }
}