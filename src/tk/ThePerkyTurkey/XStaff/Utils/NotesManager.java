package tk.ThePerkyTurkey.XStaff.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import tk.ThePerkyTurkey.XStaff.XStaff;
import tk.ThePerkyTurkey.XStaff.Utils.SmallClasses.StaffMember;
import tk.ThePerkyTurkey.XStaff.Utils.SmallClasses.Trivalue;

public class NotesManager {

	private XStaff xs;
	private Messages msg;
	private File baseFile;
	private FileConfiguration notes;

	public NotesManager(XStaff xs) {
		this.xs = xs;
		this.msg = xs.getMessages();
		this.baseFile = xs.getConfigManager().getNotesFile();
		this.notes = xs.getConfigManager().getNotes();
	}

	public void addNote(String p, CommandSender staff, String why) {

		String note = why.replaceFirst(" ", "");

		String totalnotesString = notes.getString(p + ".total-notes");
		if (totalnotesString == null) {
			notes.set(p + ".total-notes", 0);
			totalnotesString = "0";
		}
		int totalnotes = Integer.valueOf(totalnotesString) + 1;

		notes.set(p + "." + totalnotes + ".staff", staff.getName());
		notes.set(p + "." + totalnotes + ".note", note);

		notes.set(p + ".total-notes", totalnotes);
		staff.sendMessage(msg.get("noteSuccess", p));

		try {
			notes.save(baseFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void removeNote(String p, CommandSender staff, String id) {

		String totalnotesString = notes.getString(p + ".total-notes");
		if (totalnotesString == null || totalnotesString.equals("0")) {
			staff.sendMessage(msg.get("noNotes"));
			return;
		}

		int totalnotes = Integer.valueOf(totalnotesString);

		if (id == null || Integer.valueOf(id) > totalnotes) {
			notes.set(p + "." + totalnotes, null);
			notes.set(p + ".total-notes", totalnotes - 1);
			staff.sendMessage(msg.get("noteRemove", p));
		} else {
			notes.set(p + "." + id, null);
			notes.set(p + ".total-notes", totalnotes - 1);
			
			List<Trivalue> data = new ArrayList<Trivalue>();
			for(int i = Integer.valueOf(id) + 1; i < totalnotes + 1; i ++) {
				String player = notes.getString(p + "." + i + ".staff");
				String note = notes.getString(p + "." + i + ".note");
				Trivalue tri = new Trivalue(String.valueOf(i), player, note);
				data.add(tri);
			}
			
			for(Trivalue tri : data) {
				notes.set(p + "." + (Integer.valueOf(tri.getA()) - 1) + ".staff" , tri.getB());
				notes.set(p + "." + (Integer.valueOf(tri.getA()) - 1) + ".note", tri.getC());
			}
			
			notes.set(p + "." + totalnotes, null);
			staff.sendMessage(msg.get("noteRemoveId", p, id));
		}

		try {
			notes.save(baseFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Format : LinkedHashMap<Staff member, note>
	public LinkedHashMap<StaffMember, String> getNotes(String p) {
		LinkedHashMap<StaffMember, String> notesMap = new LinkedHashMap<StaffMember, String>();

		String totalNotesString = notes.getString(p + ".total-notes");
		if (totalNotesString == null || totalNotesString == "0") {
			return null;
		}

		int totalnotes = Integer.valueOf(totalNotesString);

		for (int i = 1; i < totalnotes + 1; i++) {
			notesMap.put(new StaffMember(notes.getString(p + "." + String.valueOf(i) + ".staff")),
					notes.getString(p + "." + String.valueOf(i) + ".note"));
		}

		return notesMap;
	}
}
