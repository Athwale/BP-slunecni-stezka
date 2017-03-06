package ondrej.mejzlik.suntrail.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ondrej.mejzlik.suntrail.R;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Fragment class which can show a zoomable imageview.
 */
public class ZoomableImageFragment extends Fragment {
    private PhotoViewAttacher zoomer;

    public ZoomableImageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zoomable_image, container, false);
        // Get display size to set image view size in order to be able to zoom infinitely
        DisplayMetrics displaymetrics = new DisplayMetrics();
        // Fragment is attached, onAttach is called before onCreateView
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        // Get image resource id from passed arguments if it is there
        // The map resource is always there
        // Otherwise blank fragment is opened
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("image")) {
            int imageResource = getArguments().getInt("image");
            ImageView imageView = (ImageView) (view.findViewById(R.id.zoomable_image_view));
            // Set image into image view
            imageView.setImageResource(imageResource);
            // Resize image view otherwise zooming is clipped at viewbounds
            imageView.getLayoutParams().height = displaymetrics.heightPixels;
            imageView.getLayoutParams().width = displaymetrics.widthPixels;
            // Attach zooming capabilities
            zoomer = new PhotoViewAttacher(imageView);
        }

        return view;
    }

}
