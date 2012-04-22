package morris.models;

import morris.game.Network;
import android.util.Log;

import com.skiller.api.listeners.SKOnGameMoveListener;
import com.skiller.api.operations.SKTurnBasedTools;
import com.skiller.api.responses.SKGameMoveResponse;

public class GameMove extends SKOnGameMoveListener {

	@Override
	public void onResponse(SKGameMoveResponse st) {
		if (st.getStatusCode() == 0) {// status OK

			// 1. received data:
			String chat = st.getChatLine();
			int game_state = st.getGameState();
			String game_id = st.getGameId();
			// 2. game logic
			String Opponentpayload = st.getPayload();
			Network.getInstance().handleOpponentMove(game_state, game_id,Opponentpayload);
		} else {// status ERROR
			Log.i("skiller", "could not get message from skGameMoveResponse" + st.getStatusMessage());
				// GameHandler.getInstance().showErrorDialog(st.getStatusMessage());
			//Network.getInstance().switchTurns();
			//Network.getInstance().sendInformation(st.getPayload(), SKTurnBasedTools.GAME_EVENT_MAKING_MOVE, null);
			
		}
	}

}
