package com.example.socialmedia;

import android.net.Uri;

//import com.google.firebase.dynamicinvites.kotlin.model.InviteContent;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

public class DynamicLinksUtil {

    /*public static InviteContent generateInviteContent() {
        return new InviteContent(
                "Hey check out my great app!",
                "It's like the best app ever.",
                generateContentLink());
    }*/

    // [START ddl_generate_content_link]
    public static Uri generateContentLink(Uri baseUrl) {
       // Uri baseUrl = Uri.parse("https://healthappinnovation.page.link/Eit5");
       Uri baseUrl1 = Uri.parse(String.valueOf(baseUrl));


        String domain = "https://healthappinnovation.page.link/bjYi";

        DynamicLink link = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setLink(Uri.parse(String.valueOf(baseUrl)))
                .setDomainUriPrefix(domain)
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.example.socialmedia").build())
                .buildDynamicLink();

        return link.getUri();
    }
    // [END ddl_generate_content_link]

}
