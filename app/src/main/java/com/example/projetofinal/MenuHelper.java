package com.example.projetofinal;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MenuHelper {

    public static void showPopupMenu(Context context, View anchor) {
        PopupMenu popup = new PopupMenu(context, anchor);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_massa_atomica) {
                    Intent intent = new Intent(context, massa_atomica.class);
                    context.startActivity(intent);
                    return true;
                } else if (id == R.id.nav_desenvolvedores) {
                    Intent intent = new Intent(context, DesenvolvedoresActivity.class);
                    context.startActivity(intent);
                    return true;
                }else if (id == R.id.nav_massa_molar) {
                    Intent intent = new Intent(context, MassaMolar.class);
                    context.startActivity(intent);
                    return true;
                }else if (id == R.id.nav_home) {
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    return true;
                }else if (id == R.id.nav_concentracao) {
                    Intent intent = new Intent(context, Concentracao.class);
                    context.startActivity(intent);
                    return true;
                }else if (id == R.id.nav_densidade) {
                    Intent intent = new Intent(context, Densidade.class);
                    context.startActivity(intent);
                    return true;
                }else if (id == R.id.nav_num_mol) {
                    Intent intent = new Intent(context, NumeroMol.class);
                    context.startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        popup.show();
    }
}
