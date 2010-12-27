package game;

public class SceneManager{
	
	private IScene mActive;
	
	//Should already be linked with child scenes
	public SceneManager(IScene topScene){
		mActive = topScene;
		mActive.setup();
	}
	
	public IScene getCurrentScene(){
		if(mActive.done()){
			mActive.finish();
			mActive = mActive.getParent();
			mActive.setup();
		}
		else if(mActive.childReady()){
			mActive.finish();
			mActive = mActive.getChild();
			mActive.setup();
		}
		return mActive;
	}
	
	
}