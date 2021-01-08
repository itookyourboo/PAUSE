package com.insaze.pause.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.insaze.pause.model.App
import com.insaze.pause.R


class InstalledAppsAdapter(
    val context: Context,
    val list: List<App>,
    val onItemClickListener: (app: App) -> Unit
) :
    RecyclerView.Adapter<InstalledAppsAdapter.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var mTextViewLabel: TextView = v.findViewById(R.id.Apk_Name)
        var mTextViewPackage: TextView = v.findViewById(R.id.Apk_Package_Name)
        var mImageViewIcon: ImageView = v.findViewById(R.id.packageImage) as ImageView
        var mAppSelect: CheckBox = v.findViewById(R.id.appSelect)
        var mItem: RelativeLayout = v.findViewById(R.id.item)

        fun bind(app: App) {
            val packageName: String = app.packageName
            val icon: Drawable = (app.logo
                ?: context.getDrawable(R.drawable.ic_launcher_background)) as Drawable
            val label: String = app.name

            mTextViewLabel.text = label
            mTextViewPackage.text = packageName
            mImageViewIcon.setImageDrawable(icon)
            mAppSelect.isChecked = app.blocked

            arrayOf(mItem, mAppSelect).forEach {
                it.setOnClickListener {
                    if (it == mItem) mAppSelect.isChecked = !app.blocked
                    app.blocked = !app.blocked
                    onItemClickListener(app)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.app_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}