package pt.nunopeixoto.wtest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.nunopeixoto.wtest.databinding.ItemPostalCodeBinding
import pt.nunopeixoto.wtest.db.entity.PostalCode

class PostalCodeAdapter(
    var postalCodeList: List<PostalCode>
    ): RecyclerView.Adapter<PostalCodeAdapter.MyViewHolder>() {

    fun setData(postalCodeList: List<PostalCode>) {
        this.postalCodeList = postalCodeList
        notifyDataSetChanged()
    }

    class MyViewHolder(val binding: ItemPostalCodeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPostalCodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = postalCodeList[position]
        with(holder) {
            binding.textViewName.text = item.name
            binding.textViewCode.text =
                holder.itemView.context.getString(R.string.postal_code, item.code, item.extCode)
        }
    }

    override fun getItemCount(): Int {
        return postalCodeList.size
    }
}