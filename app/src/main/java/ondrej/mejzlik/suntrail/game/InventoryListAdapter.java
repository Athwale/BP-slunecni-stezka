package ondrej.mejzlik.suntrail.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ondrej.mejzlik.suntrail.R;

/**
 * This class is an adapter which converts game items from database to rows inside ListView in the
 * inventory.
 */
public class InventoryListAdapter extends ArrayAdapter<ItemModel> {

    public InventoryListAdapter(Context context, ArrayList<ItemModel> items) {
        super(context, 0, items);
    }

    @SuppressLint("SetTextI18n")
    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        ItemModel item = getItem(position);
        // Create a new ViewHolder cache.
        ViewHolder viewHolder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            // Create a new row and ViewHolder if there is nothing to reuse
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_view_inventory_row, parent, false);
            viewHolder.itemImage = (ImageView) convertView.findViewById(R.id.row_image_view_item);
            viewHolder.itemPriceMovement = (ImageView) convertView.findViewById(R.id.row_image_view_price_movement);
            viewHolder.itemName = (TextView) convertView.findViewById(R.id.row_item_name);
            viewHolder.itemSize = (TextView) convertView.findViewById(R.id.row_item_size);
            // Add the holder to the view
            convertView.setTag(viewHolder);
        } else {
            // We are recycling the view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Fill the views fom the ViewHolder either recycled or new.
        if (item != null) {
            viewHolder.itemImage.setImageResource(item.getItemImageResId());
            // Set price movement indicator
            if (item.getPriceMovement()) {
                viewHolder.itemPriceMovement.setImageResource(R.drawable.pict_arrow_green);
            } else {
                viewHolder.itemPriceMovement.setImageResource(R.drawable.pict_arrow_red);
            }
            viewHolder.itemName.setText(item.getItemNameResId());
            viewHolder.itemSize.setText(" " + String.valueOf(item.getSize()));
        }

        // Return the completed view
        return convertView;
    }

    /**
     * This class holds the found views which are reused if possible. This saves calling
     * findViewById improving performance. If the row view is being recycled we can reuse the views
     * otherwise a new row has to be made. The holder is attached to the row by setTag.
     */
    private static class ViewHolder {
        ImageView itemImage;
        ImageView itemPriceMovement;
        TextView itemName;
        TextView itemSize;
    }

}
