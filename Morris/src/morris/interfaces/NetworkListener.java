package morris.interfaces;

import morris.game.Network;

import com.skiller.api.listeners.SKOnGameMoveListener;
import com.skiller.api.responses.SKGameMoveResponse;

public class NetworkListener extends SKOnGameMoveListener{

	@Override
	public void onResponse(SKGameMoveResponse st) {
		if (st.getStatusCode() == 0){// status OK

			//1. received data:
			String chat=st.getChatLine();
			int game_state=st.getGameState();
			String game_id=st.getGameId();
			//2. game logic
			String Opponentpayload=st.getPayload();
			Network.getInstance().handleOpponentMove(game_state, game_id, Opponentpayload);	
		}
		else
		{//status ERROR
			System.out.println("ERROR i NetworkListener");
		}	
		
	}
	

}
