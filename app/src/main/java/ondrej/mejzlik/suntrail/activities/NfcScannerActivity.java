package ondrej.mejzlik.suntrail.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ondrej.mejzlik.suntrail.R;
import ondrej.mejzlik.suntrail.utilities.PlanetIdentifier;

/**
 * MFC is not a dangerous permission, we do not have to ask for it.
 */
public class NfcScannerActivity extends Activity {
    private PendingIntent pendingIntent = null;
    private NfcAdapter nfcAdapter = null;
    private IntentFilter[] intentFiltersArray = null;
    private String[][] techListsArray = null;
    private PlanetIdentifier identifier = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_scanner);
        this.identifier = new PlanetIdentifier();

        // Animate text to fade out
        TextView sensorsActivated = (TextView) findViewById(R.id.nfc_scanner_text_view_scanner_activated);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(sensorsActivated, "alpha", 0.3f, 1f);
        fadeIn.setDuration(1000);
        fadeIn.setRepeatCount(ObjectAnimator.INFINITE);
        fadeIn.start();

        // Set planet revolutions
        ImageView animationPlanet = (ImageView) findViewById(R.id.nfc_scanner_image_view_animation_planet);
        ObjectAnimator animatorPlanet = ObjectAnimator.ofFloat(animationPlanet, "rotation", 360, 0);
        animatorPlanet.setDuration(100000);
        animatorPlanet.setRepeatCount(ObjectAnimator.INFINITE);
        animatorPlanet.setInterpolator(new LinearInterpolator());
        animatorPlanet.start();

        // Make the ship orbit the planet in opposite direction
        ImageView animationShip = (ImageView) findViewById(R.id.nfc_scanner_image_view_animation_ship);
        ObjectAnimator animatorShip = ObjectAnimator.ofFloat(animationShip, "rotation", 0, 360);
        animatorShip.setDuration(10000);
        animatorShip.setRepeatCount(ObjectAnimator.INFINITE);
        animatorShip.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorShip.start();

        this.setUpNfc();
        // This is necessary if nfc intent filter is set inside AndroidManifest.
        //this.handleNfc(getIntent());
    }

    private void setUpNfc() {
        // Create nfc adapter to read the tag
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (this.nfcAdapter == null) {
            // Device does not support NFC
            Toast.makeText(this, getString(R.string.nfc_scanner_nfc_not_supported), Toast.LENGTH_LONG).show();
        } else if (!this.nfcAdapter.isEnabled()) {
            // NFC is disabled
            Toast.makeText(this, getString(R.string.nfc_scanner_nfc_disabled), Toast.LENGTH_LONG).show();
        } else {
            // Android will save tag data into this intent
            this.pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        }

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        this.intentFiltersArray = new IntentFilter[]{ndef,};
        this.techListsArray = new String[][]{new String[]{NfcA.class.getName()}};
    }

    @Override
    public void onPause() {
        super.onPause();
        this.nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            // If the user disabled nfc then onResume can not access the nfcAdapter.
            // We close the activity and setUpNfc shows a toast.
            this.nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
        } catch (NullPointerException ex) {
            this.finish();
        }
        // If NFC is disabled while the nfc scanner activity is running and the user returns to it.
        if (!this.nfcAdapter.isEnabled()) {
            Toast.makeText(this, getString(R.string.nfc_scanner_nfc_disabled), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // If we call this method from onCreate too processing has to be done elsewhere.
        this.handleNfc(intent);
    }

    /**
     * This method handles the NFC tag, and decodes the NFC into a planet ID.
     *
     * @param intent Intent from onCreate or onNewIntent
     */
    private void handleNfc(Intent intent) {
        String tagId = this.getTagInfo(intent);
        Toast.makeText(this, tagId, Toast.LENGTH_LONG).show();
    }

    /**
     * This method returns the TAG ID
     *
     * @param intent Intent from onCreate or onNewIntent
     * @return Tag ID
     */
    private String getTagId(Intent intent) {
        String id = "";
        // NFCs used on the trail fall to the category of TECH
        if (intent != null && NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            // Get tag ID
            byte[] tagId = tag.getId();
            for (byte aTagId : tagId) {
                // Bytes in android are signed, we need unsigned bytes and convert them to string.
                id += Integer.toHexString(aTagId & 0xFF);
            }
        }
        return id;
    }

    /**
     * This method gets all tag info from the NFC tag. It is used in hidden developer mode.
     *
     * @param intent Intent from onCreate or onNewIntent
     * @return Tag ID
     */

    private String getTagInfo(Intent intent) {
        String nuid = "No ID";
        if (intent != null && NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            // Read tag info
            // General info
            String tagInfo = tag.toString() + "\n";
            // ID
            tagInfo += "Tag ID: ";
            byte[] tagId = tag.getId();
            nuid = "";
            tagInfo += "Length= " + tagId.length + "\n" + "ID: ";
            for (byte aTagId : tagId) {
                // Bytes in android are signed, we need unsigned bytes and convert them to string.
                nuid += Integer.toHexString(aTagId & 0xFF);
            }
            tagInfo += nuid;
            // Tech contents
            String[] techList = tag.getTechList();
            tagInfo += "\nTech List ";
            tagInfo += "length = " + techList.length + "\n";
            for (String aTechList : techList) {
                tagInfo += aTechList + "\n";
            }
            // Decode planet
            int planetId = this.identifier.getPlanetIdFromNfc(nuid);
            tagInfo += "Internal planet ID: " + planetId;
            tagInfo += "\nPlanet name: " + this.identifier.getPlanetName(planetId);
            // Show a dialog with the info
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(tagInfo);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return nuid;
    }
}