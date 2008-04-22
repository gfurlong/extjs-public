/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.mail.client.mvc;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.samples.mail.client.AppEvents;
import com.extjs.gxt.samples.resources.client.Folder;
import com.extjs.gxt.samples.resources.client.MailItem;
import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;
import com.extjs.gxt.ui.client.widget.table.TableItem;

public class MailListView extends View {

  private Table table;
  private Folder folder;

  public MailListView(Controller controller) {
    super(controller);
  }

  protected void handleEvent(AppEvent event) {
    if (event.type == AppEvents.ViewMailItems) {
      Folder f = (Folder) event.data;

      ContentPanel center = (ContentPanel) Registry.get("center");
      center.setHeading(f.getName());

      center.removeAll();
      center.add(table);
      center.layout(true);

      Container south = (Container) Registry.get("south");
      south.removeAll();

      if (folder != f) {
        folder = f;
        table.removeAll();
        for (int i = 0; i < f.getChildCount(); i++) {
          MailItem m = (MailItem) f.getChild(i);
          Object[] values = new Object[3];
          values[0] = m.getSender();
          values[1] = m.getEmail();
          values[2] = m.getSubject();
          TableItem item = new TableItem(values);
          item.setData(m);
          table.add(item);
        }
        if (table.getItemCount() > 0) {
          table.select(0);
        }
      } else {
        if (table.getSelection().size() > 0) {
          TableItem item = table.getSelection().get(0);
          MailItem mail = (MailItem) item.getData();
          showMailItem(mail);
        }
      }

    }
  }

  protected void initialize() {
    List<TableColumn> columns = new ArrayList<TableColumn>();
    columns.add(new TableColumn("sender", "Sender", .2f));
    columns.add(new TableColumn("email", "Email", .3f));
    columns.add(new TableColumn("subject", "Subject", .5f));

    TableColumnModel cm = new TableColumnModel(columns);

    table = new Table(cm);
    table.selectionMode = SelectionMode.MULTI;
    table.setBorders(false);

    table.addListener(Events.SelectionChange, new Listener() {

      public void handleEvent(BaseEvent be) {
        if (table.getSelection().size() > 0) {
          TableItem item = table.getSelection().get(0);
          MailItem mail = (MailItem) item.getData();
          showMailItem(mail);
        }
      }
    });
  }

  private void showMailItem(MailItem item) {
    AppEvent evt = new AppEvent(AppEvents.ViewMailItem, item);
    fireEvent(evt);
  }

}
