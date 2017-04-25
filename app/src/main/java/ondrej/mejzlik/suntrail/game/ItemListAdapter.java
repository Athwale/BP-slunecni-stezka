package ondrej.mejzlik.suntrail.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
public class ItemListAdapter extends ArrayAdapter<ItemModel> {
    // Needed for getting colors from resources
    private final Context context;

    public ItemListAdapter(Context context, ArrayList<ItemModel> items) {
        super(context, 0, items);
        this.context = context;
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
            convertView = inflater.inflate(R.layout.list_view_item_row, parent, false);
            viewHolder.itemImage = (ImageView) convertView.findViewById(R.id.row_image_view_item);
            viewHolder.itemPriceMovement = (ImageView) convertView.findViewById(R.id.row_image_view_price_movement);
            viewHolder.itemName = (TextView) convertView.findViewById(R.id.row_item_name);
            viewHolder.itemSize = (TextView) convertView.findViewById(R.id.row_item_size);
            viewHolder.itemPrice = (TextView) convertView.findViewById(R.id.row_item_price);
            viewHolder.itemPriceTitle = (TextView) convertView.findViewById(R.id.row_item_price_title);
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
            viewHolder.itemPrice.setText(" " + String.valueOf(item.getPrice()));
            // Set specific data either for items that can be bough or sold
            if (item.isInShop()) {
                // Set background
                if (item.isBought()) {
                    // The player has this in inventory, so it can be sold, set green.
                    viewHolder.itemPriceTitle.setText(R.string.list_selling_price);
                    int backgroundColor = ContextCompat.getColor(context, R.color.rowGreen);
                    convertView.setBackgroundColor(backgroundColor);
                } else if (item.canBeBought()) {
                    // The item is in the shop and can be bought, set orange.
                    viewHolder.itemPriceTitle.setText(R.string.list_price);
                    int backgroundColor = ContextCompat.getColor(context, R.color.rowOrange);
                    convertView.setBackgroundColor(backgroundColor);
                } else {
                    // The item is in shop and can not be bough, price is too high or there is not
                    // enough space in the cargo bay. Set red background.
                    viewHolder.itemPriceTitle.setText(R.string.list_price);
                    int backgroundColor = ContextCompat.getColor(context, R.color.rowRed);
                    convertView.setBackgroundColor(backgroundColor);
                }
            } else {
                // We are in inventory, display green and show current price.
                viewHolder.itemPriceTitle.setText(R.string.list_selling_price);
                int backgroundColor = ContextCompat.getColor(context, R.color.rowGreen);
                convertView.setBackgroundColor(backgroundColor);
            }
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
        TextView itemPrice;
        TextView itemPriceTitle;
    }

}
