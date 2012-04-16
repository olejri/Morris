package morris.models;

import java.util.Timer;
import java.util.TimerTask;

import morris.game.controller.GameController;

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
			if(GameController.getInstance().getSkApplication().getUserManager().getCurrentUsername().equals(ownerUsername)){
				System.out.println("User: "+GameController.getInstance().getSkApplication().getUserManager().getCurrentUsername().toString());
				int pot = st.getPot();
				SKUser guest = st.getGuest();
				SKUser owner = st.getOwner();
				String game_id = st.getGameId();
				
				GameController.getInstance().setWaiting_for_opponent(false);
				GameController.getInstance().setOwner(owner);
				GameController.getInstance().setGuest(guest);
				GameController.getInstance().setPot(pot);
				GameController.getInstance().setGame_id(game_id);
				GameController.getInstance().setGameOwner(true);
				
				return;
			}		
			
			// Guest game
			GameController.getInstance().setWaiting_for_opponent(false);
			GameController.getInstance().setProgressDialog(ProgressDialog.show(GameController.getInstance().getMenuContext(),"Please wait" , "Connecting to the game...", true));
		
			class DismissProgressDialogTask extends TimerTask{
				@Override
				public void run() {
					GameController.getInstance().getProgressDialog().dismiss();
					
				}
			}
			
			GameController.getInstance().setTimer(new Timer());
			GameController.getInstance().getTimer().schedule(new DismissProgressDialogTask(), 15000);
		
			int pot=st.getPot();
			SKUser guest=st.getGuest();
			SKUser owner=st.getOwner();
			String game_id=st.getGameId();
			
			GameController.getInstance().setGameStarted(true);
			GameController.getInstance().setPrinted(false);
			GameController.getInstance().setGame_id(game_id);
			GameController.getInstance().setOwner(owner);
			GameController.getInstance().setGuest(guest);
			GameController.getInstance().setPot(pot);	
			GameController.getInstance().setGameOwner(false);
			
			return;
		}
	}

}
