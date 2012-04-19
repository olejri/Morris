package morris.models;

import java.util.Timer;
import java.util.TimerTask;

import morris.game.Network;
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
			if(Network.getInstance().getSkApplication().getUserManager().getCurrentUsername().equals(ownerUsername)){
				System.out.println("User: "+Network.getInstance().getSkApplication().getUserManager().getCurrentUsername().toString());
				int pot = st.getPot();
				SKUser guest = st.getGuest();
				SKUser owner = st.getOwner();
				String game_id = st.getGameId();
				
				Network.getInstance().setWaiting_for_opponent(false);
				Network.getInstance().setOwner(owner);
				Network.getInstance().setGuest(guest);
				Network.getInstance().setPot(pot);
				Network.getInstance().setGame_id(game_id);
				Network.getInstance().setGameOwner(true);
				Network.getInstance().startGame();
				return;
			}		
			
			// Guest game
			Network.getInstance().setWaiting_for_opponent(false);
			Network.getInstance().setProgressDialog(ProgressDialog.show(Network.getInstance().getMenuContext(),"Please wait" , "Connecting to the game...", true));
		
			class DismissProgressDialogTask extends TimerTask{
				@Override
				public void run() {
					Network.getInstance().getProgressDialog().dismiss();
					
				}
			}
			
			Network.getInstance().clearGame();
			
			Network.getInstance().setTimer(new Timer());
			Network.getInstance().getTimer().schedule(new DismissProgressDialogTask(), 15000);
		
			int pot=st.getPot();
			SKUser guest=st.getGuest();
			SKUser owner=st.getOwner();
			String game_id=st.getGameId();
			
			Network.getInstance().setGameStarted(true);
			Network.getInstance().setPrinted(false);
			Network.getInstance().setGame_id(game_id);
			Network.getInstance().setOwner(owner);
			Network.getInstance().setGuest(guest);
			Network.getInstance().setPot(pot);	
			Network.getInstance().setGameOwner(false);
			Network.getInstance().setTurn(1);
			Network.getInstance().startGame();
			return;
		}
	}

}
