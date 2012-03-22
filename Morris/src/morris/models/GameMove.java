package morris.models;

import morris.game.GameHandler;

import com.skiller.api.listeners.SKOnGameMoveListener;
import com.skiller.api.responses.SKGameMoveResponse;

public class GameMove extends SKOnGameMoveListener{
	
	@Override
	public void onResponse(SKGameMoveResponse st)
	{
		if (st.getStatusCode() == 0)
		{// status OK

			//1. received data:
			String chat=st.getChatLine();
			int game_state=st.getGameState();
			String game_id=st.getGameId();
			//2. game logic
			String Opponentpayload=st.getPayload();
			GameHandler.getInstance().handleOpponentMove(game_state, game_id, Opponentpayload);	
		}
		else
		{//status ERROR
			//GameHandler.getInstance().showErrorDialog(st.getStatusMessage());
		}	
	}

}
