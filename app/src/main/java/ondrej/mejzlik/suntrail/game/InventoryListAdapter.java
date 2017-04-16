package ondrej.mejzlik.suntrail.game;

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

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        ItemModel item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_inventory_row, parent, false);
        }

        // Get views in the row and set data into them
        ImageView itemImage = (ImageView) convertView.findViewById(R.id.row_image_view_item);
        ImageView itemPriceMovement = (ImageView) convertView.findViewById(R.id.row_image_view_price_movement);
        TextView itemName = (TextView) convertView.findViewById(R.id.row_item_name);
        TextView itemSize = (TextView) convertView.findViewById(R.id.row_item_size);

        if (item != null) {
            itemImage.setImageResource(item.getItemImageResId());
            // Set price movement indicator
            if (item.getPriceMovement()) {
                itemPriceMovement.setImageResource(R.drawable.pict_arrow_green);
            } else {
                itemPriceMovement.setImageResource(R.drawable.pict_arrow_red);
            }
            itemName.setText(item.getItemNameResId());
            itemSize.setText(item.getSize());
        }

        // Return the completed view
        return convertView;
    }

}
