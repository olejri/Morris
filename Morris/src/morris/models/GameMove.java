package morris.models;

import morris.game.Network;
import android.util.Log;

import com.skiller.api.listeners.SKOnGameMoveListener;
import com.skiller.api.operations.SKTurnBasedTools;
import com.skiller.api.responses.SKGameMoveResponse;

public class GameMove extends SKOnGameMoveListener {

	@Override
	public void onResponse(SKGameMoveResponse st) {
		
		String state = "Default";
		switch (st.getGameState()) {
		case SKTurnBasedTools.GAME_EVENT_QUIT_GAME:
			state = "GAME_EVENT_QUIT_GAME";
			//Network.getInstance().setServerEndGameresponse(true);
			break;
		case SKTurnBasedTools.GAME_STATE_WON:
			state = "GAME_STATE_WON";
			Network.getInstance().setServerEndGameresponse(true);
			break;

		case SKTurnBasedTools.GAME_STATE_LOST:
			state = "GAME_STATE_LOST";
			Network.getInstance().setServerEndGameresponse(true);
			break;

		case SKTurnBasedTools.GAME_STATE_TIED:
			state = "GAME_STATE_TIED";
			Network.getInstance().setServerEndGameresponse(true);
			break;

		case SKTurnBasedTools.GAME_STATE_ARE_YOU_HERE:

		default :

		}		
		
		if (st.getStatusCode() == 0) {// status OK
			// 1. received data:
			String chat = st.getChatLine();
			int game_state = st.getGameState();
			String game_id = st.getGameId();
			// 2. game logic
			String Opponentpayload = st.getPayload();
			Network.getInstance().handleOpponentMove(game_state, game_id,Opponentpayload);
			
			

			
			
			
		} else {// status ERROR
		}
	}

}
