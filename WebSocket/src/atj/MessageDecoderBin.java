package atj;

import java.nio.ByteBuffer;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import atj.Message;

public class MessageDecoderBin implements Decoder.Binary<Message>{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Message decode(ByteBuffer buffer) throws DecodeException {

		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean willDecode(ByteBuffer arg0) {
	
		return true;
	}

}
