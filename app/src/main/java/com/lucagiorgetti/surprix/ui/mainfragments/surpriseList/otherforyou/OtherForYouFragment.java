package com.lucagiorgetti.surprix.ui.mainfragments.otherforyou;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.model.User;
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.doublelist.DoubleRecyclerAdapter;
import com.lucagiorgetti.surprix.utility.BaseFragment;

import java.util.List;
import java.util.Locale;

public class OtherForYouFragment extends BaseFragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_other_for_you, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.other_for_you_recycler);
        ProgressBar progress = root.findViewById(R.id.other_for_you_loading);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DoubleRecyclerAdapter adapter = new DoubleRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        OtherForYouViewModel mViewModel = new ViewModelProvider(this).get(OtherForYouViewModel.class);
        String ownerUsername = OtherForYouFragmentArgs.fromBundle(requireArguments()).getOwnerUsername();
        String ownerEmail = OtherForYouFragmentArgs.fromBundle(requireArguments()).getOwnerEmail();

        FloatingActionButton fab = root.findViewById(R.id.other_for_you_fab_mail);

        mViewModel.getOtherForYou(ownerUsername).observe(getViewLifecycleOwner(), surprises -> {
            if (!surprises.isEmpty()) {
                fab.setVisibility(View.VISIBLE);
                adapter.submitList(surprises);
                adapter.notifyDataSetChanged();
                fab.setOnClickListener(v -> {
                    sendEmailToUser(ownerEmail, surprises);
                });
            } else {
                fab.setVisibility(View.GONE);
            }
        });

        mViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> progress.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        setTitle(String.format(Locale.getDefault(), getString(R.string.other_for_you_title), ownerUsername));
        return root;
    }

    private void sendEmailToUser(String ownerEmail, List<Surprise> surprises) {
        User currentUser = SurprixApplication.getInstance().getCurrentUser();
        String to = ownerEmail;
        String subject = SurprixApplication.getInstance().getString(R.string.mail_subject, currentUser.getUsername());

        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (Surprise surprise : surprises) {
            sb.append(String.format(Locale.getDefault(), "<li>%s - %s<li>", surprise.getCode(), surprise.getDescription()));
        }
        sb.append("</ul>");

        String html = sb.toString();

        Spanned body;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            body = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            body = Html.fromHtml(html);
        }

        sendMail(to, subject, body);
    }

    private void sendMail(String recipient, String subject, Spanned html_body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/rfc822");
        intent.setData(Uri.parse("mailto:" + recipient));
        if (subject != null && !subject.isEmpty()) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (html_body != null && html_body.length() > 0) {
            intent.putExtra(Intent.EXTRA_TEXT, html_body);
        }
        startActivity(Intent.createChooser(intent, getString(R.string.email_app_chooser)));
    }
}
