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
package com.genonbeta.TrebleShot.fragment.external

/**
 * created by: Veli
 * date: 16.03.2018 15:46
 */
class GitHubContributorsListFragment :
    DynamicRecyclerViewFragment<ContributorObject?, RecyclerViewAdapter.ViewHolder?, ContributorListAdapter?>() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return generateDefaultView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdapter = ContributorListAdapter(context)
        setEmptyListImage(R.drawable.ic_github_circle_white_24dp)
        setEmptyListText(getString(R.string.mesg_noInternetConnection))
        useEmptyListActionButton(getString(R.string.butn_refresh)) { v: View? -> refreshList() }
        listView.isNestedScrollingEnabled = true
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 1)
    }

    class ContributorObject(var name: String, var url: String, var urlAvatar: String)
    class ContributorListAdapter(context: Context?) :
        RecyclerViewAdapter<ContributorObject, RecyclerViewAdapter.ViewHolder>(context) {
        private val mList: MutableList<ContributorObject> = ArrayList()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val holder = ViewHolder(
                inflater.inflate(
                    R.layout.list_contributors, parent,
                    false
                )
            )
            holder.itemView.findViewById<View>(R.id.visitView).setOnClickListener(View.OnClickListener { v: View? ->
                val contributorObject = list[holder.adapterPosition]
                context.startActivity(
                    Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(String.format(AppConfig.URI_GITHUB_PROFILE, contributorObject.name)))
                )
            })
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val contributorObject = list[position]
            val textView: TextView = holder.itemView.findViewById<TextView>(R.id.text)
            val imageView = holder.itemView.findViewById<ImageView>(R.id.image)
            textView.setText(contributorObject.name)
            GlideApp.with(context)
                .load(contributorObject.urlAvatar)
                .override(90)
                .circleCrop()
                .into(imageView)
        }

        override fun onLoad(): List<ContributorObject> {
            val contributorObjects: MutableList<ContributorObject> = ArrayList()
            val server = RemoteServer(AppConfig.URI_REPO_APP_CONTRIBUTORS)
            try {
                val result = server.connect(null, null)
                val releases = JSONArray(result)
                if (releases.length() > 0) {
                    for (iterator in 0 until releases.length()) {
                        val currentObject = releases.getJSONObject(iterator)
                        contributorObjects.add(
                            ContributorObject(
                                currentObject.getString("login"),
                                currentObject.getString("url"),
                                currentObject.getString("avatar_url")
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return contributorObjects
        }

        override fun onUpdate(passedItem: List<ContributorObject>) {
            synchronized(list) {
                list.clear()
                list.addAll(passedItem)
            }
        }

        override fun getItemId(i: Int): Long {
            return 0
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun getList(): MutableList<ContributorObject> {
            return mList
        }
    }
}