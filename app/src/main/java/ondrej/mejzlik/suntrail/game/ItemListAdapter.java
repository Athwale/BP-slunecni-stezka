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
            viewHolder.itemName = (TextView) convertView.findViewById(R.id.row_item_name);
            viewHolder.itemSizeTitle = (TextView) convertView.findViewById(R.id.row_item_size_title);
            viewHolder.itemSize = (TextView) convertView.findViewById(R.id.row_item_size);
            viewHolder.itemPriceTitle = (TextView) convertView.findViewById(R.id.row_item_price_title);
            viewHolder.itemPrice = (TextView) convertView.findViewById(R.id.row_item_price);
            viewHolder.itemPriceCr = (TextView) convertView.findViewById(R.id.row_item_price_cr);
            viewHolder.itemPriceMovement = (ImageView) convertView.findViewById(R.id.row_image_view_price_movement);
            viewHolder.itemPriceMovementText = (TextView) convertView.findViewById(R.id.row_text_view_price_movement);
            viewHolder.itemOperation = (TextView) convertView.findViewById(R.id.row_item_operation);
            viewHolder.itemOriginalPriceTitle = (TextView) convertView.findViewById(R.id.row_item_original_price_title);
            viewHolder.itemOriginalPrice = (TextView) convertView.findViewById(R.id.row_item_original_price);
            viewHolder.itemOriginalPriceCr = (TextView) convertView.findViewById(R.id.row_item_original_price_cr);
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
                viewHolder.itemPriceMovementText.setText(R.string.list_price_rise);
            } else {
                viewHolder.itemPriceMovement.setImageResource(R.drawable.pict_arrow_red);
                viewHolder.itemPriceMovementText.setText(R.string.list_price_fall);
            }
            viewHolder.itemName.setText(item.getItemNameResId());
            viewHolder.itemSize.setText(" " + String.valueOf(item.getSize()));
            viewHolder.itemPrice.setText(" " + String.valueOf(item.getPrice()));
            viewHolder.itemOriginalPrice.setText(" " + String.valueOf(item.getOriginalPrice()));
            // Set specific data either for items that can be bough or sold
            if (item.isInShop()) {
                // Set background and set size title to be Size:
                viewHolder.itemSizeTitle.setText(R.string.list_size);
                viewHolder.itemPriceMovement.setVisibility(View.VISIBLE);
                if (!item.isSaleable()) {
                    // We bough the item in this shop and must not be able to sell it back
                    viewHolder.itemOperation.setText(R.string.list_can_not_sell);
                    viewHolder.itemOperation.setTextColor(ContextCompat.getColor(context, R.color.textRed));
                    viewHolder.itemPriceTitle.setText(R.string.list_current_price);
                    viewHolder.itemPriceMovementText.setVisibility(View.VISIBLE);
                    // Hide unnecessary information original price is the same as current price
                    // when buying
                    viewHolder.itemOriginalPriceTitle.setVisibility(View.GONE);
                    viewHolder.itemOriginalPrice.setVisibility(View.GONE);
                    viewHolder.itemOriginalPriceCr.setVisibility(View.GONE);
                    int backgroundColor = ContextCompat.getColor(context, R.color.rowGreen);
                    convertView.setBackgroundColor(backgroundColor);
                } else if (item.isBought()) {
                    // The player has this in inventory, so it can be sold, set green.
                    viewHolder.itemOperation.setText(R.string.list_sell);
                    viewHolder.itemOperation.setTextColor(ContextCompat.getColor(context, R.color.textGreen));
                    viewHolder.itemOriginalPriceTitle.setVisibility(View.VISIBLE);
                    viewHolder.itemPriceTitle.setText(R.string.list_current_price);
                    viewHolder.itemOriginalPrice.setVisibility(View.VISIBLE);
                    viewHolder.itemOriginalPriceCr.setVisibility(View.VISIBLE);
                    int backgroundColor = ContextCompat.getColor(context, R.color.rowGreen);
                    viewHolder.itemPriceMovementText.setVisibility(View.VISIBLE);
                    convertView.setBackgroundColor(backgroundColor);
                } else if (item.canBeBought()) {
                    // The item is in the shop and can be bought, set orange.
                    viewHolder.itemOperation.setText(R.string.list_buy);
                    // Hide unnecessary information original price is the same as current price
                    // when buying
                    viewHolder.itemOriginalPriceTitle.setVisibility(View.GONE);
                    viewHolder.itemOriginalPrice.setVisibility(View.GONE);
                    viewHolder.itemOriginalPriceCr.setVisibility(View.GONE);
                    viewHolder.itemOperation.setTextColor(ContextCompat.getColor(context, R.color.textBlack));
                    viewHolder.itemPriceMovementText.setVisibility(View.VISIBLE);
                    // Change current price to simple "Price"
                    viewHolder.itemPriceTitle.setText(R.string.list_price);
                    int backgroundColor = ContextCompat.getColor(context, R.color.rowOrange);
                    convertView.setBackgroundColor(backgroundColor);
                } else {
                    // The item is in shop and can not be bough, price is too high or there is not
                    // enough space in the cargo bay. Set red background.
                    viewHolder.itemOperation.setText(R.string.list_can_not_buy);
                    viewHolder.itemOperation.setTextColor(ContextCompat.getColor(context, R.color.textRed));
                    // Hide unnecessary information original price is the same as current price
                    // when buying
                    viewHolder.itemOriginalPriceTitle.setVisibility(View.GONE);
                    viewHolder.itemOriginalPrice.setVisibility(View.GONE);
                    viewHolder.itemOriginalPriceCr.setVisibility(View.GONE);
                    viewHolder.itemPriceMovementText.setVisibility(View.VISIBLE);
                    // Change current price to simple "Price"
                    viewHolder.itemPriceTitle.setText(R.string.list_price);
                    int backgroundColor = ContextCompat.getColor(context, R.color.rowRed);
                    convertView.setBackgroundColor(backgroundColor);
                }
            } else if (item.isShip()) {
                viewHolder.itemSizeTitle.setText(context.getResources().getString(R.string.inventory_cargo_bay) + ":");
                viewHolder.itemPriceTitle.setText(R.string.list_price);
                viewHolder.itemPriceMovement.setVisibility(View.GONE);
                // Hide unnecessary information original price is the same as current price
                // when buying
                viewHolder.itemOriginalPriceTitle.setVisibility(View.GONE);
                viewHolder.itemOriginalPrice.setVisibility(View.GONE);
                viewHolder.itemOriginalPriceCr.setVisibility(View.GONE);
                viewHolder.itemPriceMovementText.setVisibility(View.GONE);
                int backgroundColor;
                if (item.canBeBought()) {
                    backgroundColor = ContextCompat.getColor(context, R.color.rowBlue);
                    viewHolder.itemOperation.setText(R.string.list_buy);
                    viewHolder.itemOperation.setTextColor(ContextCompat.getColor(context, R.color.textBlack));
                } else {
                    viewHolder.itemOperation.setText(R.string.list_can_not_buy);
                    viewHolder.itemOperation.setTextColor(ContextCompat.getColor(context, R.color.textRed));
                    backgroundColor = ContextCompat.getColor(context, R.color.rowRed);
                }
                convertView.setBackgroundColor(backgroundColor);
            } else {
                // We are in inventory, display green and show current selling price.
                viewHolder.itemSizeTitle.setText(R.string.list_size);
                viewHolder.itemOperation.setVisibility(View.GONE);
                viewHolder.itemOriginalPriceTitle.setVisibility(View.VISIBLE);
                viewHolder.itemOriginalPriceTitle.setText(R.string.list_bought_for);
                viewHolder.itemOriginalPrice.setVisibility(View.VISIBLE);
                viewHolder.itemOriginalPriceCr.setVisibility(View.VISIBLE);
                viewHolder.itemPriceMovementText.setVisibility(View.VISIBLE);
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
        TextView itemPriceMovementText;
        TextView itemName;
        TextView itemSize;
        TextView itemSizeTitle;
        TextView itemPriceTitle;
        TextView itemPrice;
        TextView itemPriceCr;
        TextView itemOperation;
        TextView itemOriginalPriceTitle;
        TextView itemOriginalPrice;
        TextView itemOriginalPriceCr;
    }

}
