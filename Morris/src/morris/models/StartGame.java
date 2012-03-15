package morris.models;

import java.util.Timer;
import java.util.TimerTask;

import morris.game.GameHandler;

import android.app.ProgressDialog;

import com.skiller.api.items.SKUser;
import com.skiller.api.listeners.SKOnGameStartedListener;
import com.skiller.api.responses.SKGameStartedResponse;

public class StartGame extends SKOnGameStartedListener {

	@Override
	public void onResponse(SKGameStartedResponse st) {
		// Status OK
		System.out.println("Statuskode" +st.getStatusCode());
		if(st.getStatusCode() == 0){
			System.out.println("Statuskode 0");
			// Getting username of the owner of the game
			SKUser ownerUser = st.getOwner();
			String ownerUsername = ownerUser.getUserName();
			
			// Checking if the current user is the owner of the game
			if(GameHandler.getInstance().getSkApplication().getUserManager().getCurrentUsername().equals(ownerUsername)){
				System.out.println("User: "+GameHandler.getInstance().getSkApplication().getUserManager().getCurrentUsername().toString());
				int pot = st.getPot();
				SKUser guest = st.getGuest();
				SKUser owner = st.getOwner();
				String game_id = st.getGameId();
				
				GameHandler.getInstance().setWaiting_for_opponent(false);
				GameHandler.getInstance().setOwner(owner);
				GameHandler.getInstance().setGuest(guest);
				GameHandler.getInstance().setPot(pot);
				GameHandler.getInstance().setGame_id(game_id);
				GameHandler.getInstance().setGameOwner(true);
				
				return;
			}		
			
			// Guest game
			GameHandler.getInstance().setWaiting_for_opponent(false);
			GameHandler.getInstance().setProgressDialog(ProgressDialog.show(GameHandler.getInstance().getMenuContext(),"Please wait" , "Connecting to the game...", true));
		
			class DismissProgressDialogTask extends TimerTask{
				@Override
				public void run() {
					GameHandler.getInstance().getProgressDialog().dismiss();
					
				}
			}
			
			GameHandler.getInstance().setTimer(new Timer());
			GameHandler.getInstance().getTimer().schedule(new DismissProgressDialogTask(), 15000);
		
			int pot=st.getPot();
			SKUser guest=st.getGuest();
			SKUser owner=st.getOwner();
			String game_id=st.getGameId();
			
			//GameHandler.getInstance().setGameStarted(true);
			//GameHandler.getInstance().setPrinted(false);
			GameHandler.getInstance().setGame_id(game_id);
			GameHandler.getInstance().setOwner(owner);
			GameHandler.getInstance().setGuest(guest);
			GameHandler.getInstance().setPot(pot);	
			GameHandler.getInstance().setGameOwner(false);
			
			return;
		}
	}

}
