package ondrej.mejzlik.suntrail.game;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import ondrej.mejzlik.suntrail.R;

/**
 * This class is an adapter used to fill data into list view rows in game inventory and shop
 * fragments.
 */
public class ItemListCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;

    public ItemListCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        // list_view_custom_row is the layout for each row.
        return cursorInflater.inflate(R.layout.list_view_custom_row, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
