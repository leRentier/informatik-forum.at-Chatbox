package informatikforum.chatbox.dao;

import informatikforum.chatbox.R;
import informatikforum.chatbox.entity.Smiley;

import java.util.ArrayList;
import java.util.List;



/**
 * SmileyDao implementation using a single CSV file as datasource.
 * The class is implemented using the Singleton-Pattern.
 * 
 * @author emptyvi
 * @version 1.0.0
 */
public class SmileyData{
	

	private final String EXCEPTION_SMILEY_NOT_FOUND = "The requested smiley can't be found in the smiley-data.";
	
	/**
	 * Contains all available smileys. Initialized in the constructor.
	 */
	private ArrayList<Smiley> smileys;
	
	/**
	 * The only instance of this class.
	 * Used for the Singleton-pattern.
	 */
	private static SmileyData instance = null;
	
	
	/**
	 * Private constructor.
	 * Used for Singleton-pattern.
	 */
	private SmileyData(){
		smileys = readInSmileys();
	}
	
	
	/**
	 * Returns the only instance of this class.
	 * Used for the Singleton-pattern.
	 */
	public static SmileyData getInstance() throws DaoException{
		if(instance == null){
			instance = new SmileyData();
		}
		
		return instance;
	}
	
	private ArrayList<Smiley> readInSmileys(){
		
		ArrayList<Smiley> smileys = new ArrayList<Smiley>();
		
		smileys.add(new Smiley("Big Grin",":D","http://www.informatik-forum.at/pics/nb/smilies/biggrin.gif", R.drawable.biggrin));
		smileys.add(new Smiley("Ahhh",":ahhh:","http://www.informatik-forum.at/pics/nb/smilies/ahhh.gif", R.drawable.ahhh));
		smileys.add(new Smiley("Devil",":devil:","http://www.informatik-forum.at/pics/nb/smilies/devil.gif", R.drawable.devil));
		smileys.add(new Smiley("wave",":wave:","http://www.informatik-forum.at/pics/nb/smilies/winken.gif", R.drawable.winken));
		smileys.add(new Smiley("keeps silent",":X","http://www.informatik-forum.at/images/nb/smilies/shocked.gif", R.drawable.shocked));
		smileys.add(new Smiley("Embarrassment",":o","http://www.informatik-forum.at/pics/nb/smilies/redface.gif", R.drawable.redface));
		smileys.add(new Smiley("Confused",":confused:","http://www.informatik-forum.at/pics/nb/smilies/confused.gif", R.drawable.confused));
		smileys.add(new Smiley("disturbed",":distur:","http://www.informatik-forum.at/pics/nb/smilies/disturbed.gif", R.drawable.disturbed));
		smileys.add(new Smiley("Aushecken",":ausheck:","http://www.informatik-forum.at/pics/nb/smilies/ausheck.gif", R.drawable.ausheck));
		smileys.add(new Smiley("Frown",":(","http://www.informatik-forum.at/pics/nb/smilies/frown.gif", R.drawable.frown));
		smileys.add(new Smiley("wave",":wave2:","http://www.informatik-forum.at/pics/nb/smilies/hihi.gif", R.drawable.hihi));
		smileys.add(new Smiley("Smilie",":)","http://www.informatik-forum.at/pics/nb/smilies/smile.gif", R.drawable.smile));
		smileys.add(new Smiley("Mad",":mad:","http://www.informatik-forum.at/pics/nb/smilies/mad.gif", R.drawable.mad));
		smileys.add(new Smiley("verycool",":verycool:","http://www.informatik-forum.at/pics/nb/smilies/verycool.gif", R.drawable.verycool));
		smileys.add(new Smiley("Engel",":engel:","http://www.informatik-forum.at/pics/nb/smilies/engel2.gif", R.drawable.engel2));
		smileys.add(new Smiley("puke",":puke:","http://www.informatik-forum.at/pics/nb/smilies/puke.gif", R.drawable.puke));
		smileys.add(new Smiley("Roll Eyes (Sarcastic)",":rolleyes:","http://www.informatik-forum.at/pics/nb/smilies/rolleyes.gif", R.drawable.rolleyes));
		smileys.add(new Smiley("tongue1",":tongue1:","http://www.informatik-forum.at/pics/nb/smilies/tongue1.gif", R.drawable.tongue1));
		smileys.add(new Smiley("Sudern",":sudern:","http://www.informatik-forum.at/pics/nb/smilies/shout.gif", R.drawable.shout));
		smileys.add(new Smiley("shiner",":shiner:","http://www.informatik-forum.at/pics/nb/smilies/shinner.gif", R.drawable.shinner));
		smileys.add(new Smiley("Cool",":cool:","http://www.informatik-forum.at/pics/nb/smilies/cool.gif", R.drawable.cool));
		smileys.add(new Smiley("thumb",":thumb:","http://www.informatik-forum.at/pics/nb/smilies/thumb.gif", R.drawable.thumb));
		smileys.add(new Smiley("Traurig",":wein:","http://www.informatik-forum.at/pics/nb/smilies/traurig.gif", R.drawable.traurig));
		smileys.add(new Smiley("Stick Out Tongue",":p","http://www.informatik-forum.at/pics/nb/smilies/tongue.gif", R.drawable.tongue));
		smileys.add(new Smiley("coolsmile",":coolsmile:","http://www.informatik-forum.at/pics/nb/smilies/coolsmile.gif", R.drawable.coolsmile));
		smileys.add(new Smiley("omg",":omg:","http://www.informatik-forum.at/pics/ob/smilies/omg_2.gif", R.drawable.omg_2));
		smileys.add(new Smiley("Wink",";)","http://www.informatik-forum.at/pics/nb/smilies/wink.gif", R.drawable.wink));
		smileys.add(new Smiley("coolgrim",":coolgrim:","http://www.informatik-forum.at/pics/nb/smilies/coolgrim.gif", R.drawable.coolgrim));
		smileys.add(new Smiley("shinner",":shinner:","http://www.informatik-forum.at/pics/nb/smilies/shinner.gif", R.drawable.shinner));
		smileys.add(new Smiley("Lauscher",":lauscher:","http://www.informatik-forum.at/pics/nb/smilies/lauscher.gif", R.drawable.lauscher));
		smileys.add(new Smiley("Traurig",":traurig:","http://www.informatik-forum.at/pics/nb/smilies/traurig.gif", R.drawable.traurig));
		smileys.add(new Smiley("Catwoman",":catwoman:","http://www.informatik-forum.at/pics/nb/smilies/catwoman.gif", R.drawable.catwoman));
		smileys.add(new Smiley("multibounce",":multibounce:","http://www.informatik-forum.at/images/nb/smilies/aola.gif", R.drawable.aola));
		smileys.add(new Smiley("rock",":rock:","http://www.informatik-forum.at/pics/nb/smilies/rock.gif", R.drawable.rock));
		smileys.add(new Smiley("highfive",":hf:","http://www.informatik-forum.at/pics/nb/smilies/highfive.gif", R.drawable.highfive));
		smileys.add(new Smiley("Head-Wall",":hewa:","http://www.informatik-forum.at/pics/nb/smilies/wallbash.gif", R.drawable.wallbash));
		smileys.add(new Smiley("WTF",":wtf:","http://www.informatik-forum.at/pics/nb/smilies/wtfb2tb.gif", R.drawable.wtfb2tb));
		smileys.add(new Smiley("yippie",":yippie:","http://www.informatik-forum.at/pics/nb/smilies/yippie.gif", R.drawable.yippie));
		smileys.add(new Smiley("multishiner",":multishiner:","http://www.informatik-forum.at/images/nb/smilies/Multishinner.gif", R.drawable.multishinner));
		smileys.add(new Smiley("highfive",":^101:","http://www.informatik-forum.at/pics/nb/smilies/highfive.gif", R.drawable.highfive));
		smileys.add(new Smiley("phaser",":phaser:","http://www.informatik-forum.at/images/nb/smilies/phaser.gif", R.drawable.phaser));
		smileys.add(new Smiley("Eek2",":eek2:","http://www.informatik-forum.at/pics/nb/smilies/eek2.gif", R.drawable.eek2));
		smileys.add(new Smiley("zwinker",":zwinker:","http://www.informatik-forum.at/pics/nb/smilies/zwinker.gif", R.drawable.zwinker));
		smileys.add(new Smiley("borg",":borg:","http://www.informatik-forum.at/pics/nb/smilies/borg.gif", R.drawable.borg));
		smileys.add(new Smiley("Applaus",":applaus:","http://www.informatik-forum.at/pics/nb/smilies/applaus.gif", R.drawable.applaus));
		smileys.add(new Smiley("druegg aka hug",":druegg:","http://www.informatik-forum.at/pics/nb/smilies/knuddel.gif", R.drawable.knuddel));
		smileys.add(new Smiley("No Devil Banana",":nodb:","http://www.informatik-forum.at/pics/nb/smilies/NoDevilBanana.gif", R.drawable.nodevilbanana));
		smileys.add(new Smiley("highfive",":highfive:","http://www.informatik-forum.at/pics/nb/smilies/highfive.gif", R.drawable.highfive));
		smileys.add(new Smiley("carrot",":carrot:","http://www.informatik-forum.at/images/nb/smilies/karotte.gif", R.drawable.karotte));
		smileys.add(new Smiley("facepalm",":facepalm:","http://www.informatik-forum.at/images/nb/smilies/facepalm.gif", R.drawable.facepalm));
		smileys.add(new Smiley("cuss",":cuss:","http://www.informatik-forum.at/pics/nb/smilies/cuss.gif", R.drawable.cuss));
		smileys.add(new Smiley("devil banana",":db:","http://www.informatik-forum.at/pics/nb/smilies/devil-banana.gif", R.drawable.devil_banana));
		smileys.add(new Smiley(":nohail:",":nohail:","http://www.informatik-forum.at/images/ob/smilies/antihail.png", R.drawable.antihail));
		smileys.add(new Smiley("hibbelig",":hibbelig:","http://www.informatik-forum.at/pics/nb/smilies/present.gif", R.drawable.present));
		smileys.add(new Smiley("banana",":banana:","http://www.informatik-forum.at//images/nb/smilies/banane.gif", R.drawable.banane));
		smileys.add(new Smiley("xD","xD","http://www.informatik-forum.at/pics/nb/smilies/lol.gif", R.drawable.lol));
		smileys.add(new Smiley("LOL",":lol:","http://www.informatik-forum.at/pics/nb/smilies/lol.gif", R.drawable.lol));
		smileys.add(new Smiley("Sleep",":zzz:","http://www.informatik-forum.at/pics/nb/smilies/zzz.gif", R.drawable.zzz));
		smileys.add(new Smiley("knutsch",":knutsch:","http://www.informatik-forum.at/images/nb/smilies/knutsch.gif", R.drawable.knutsch));
		smileys.add(new Smiley("multihail",":multihail:","http://www.informatik-forum.at/images/ob/smilies/multihail.gif", R.drawable.multihail));
		smileys.add(new Smiley("poke",":poke:","http://www.informatik-forum.at/images/nb/smilies/poke.gif", R.drawable.poke));
		smileys.add(new Smiley("snowball",":snowball:","http://www.informatik-forum.at/images/smilies/snowball.gif", R.drawable.snowball));
		smileys.add(new Smiley("rofl",":rofl:","http://www.informatik-forum.at/pics/nb/smilies/rofl.gif", R.drawable.rofl));
		smileys.add(new Smiley("Grad Aufgestanden",":awake:","http://www.informatik-forum.at/pics/nb/smilies/getup.gif", R.drawable.getup));
		smileys.add(new Smiley("*popcorn*",":popcorn:","http://www.informatik-forum.at/images/nb/smilies/popcorn.gif", R.drawable.popcorn));
		smileys.add(new Smiley("gute nacht",":gn8:","http://www.informatik-forum.at/images/ob/smilies/gn8.gif", R.drawable.gn8));
		smileys.add(new Smiley("dancing devil",":dd:","http://www.informatik-forum.at/images/nb/smilies/dancingdevil.gif", R.drawable.dancingdevil));
		smileys.add(new Smiley("cheer",":cheer:","http://www.informatik-forum.at//images/nb/smilies/Cheerleader_by_CookiemagiK.gif", R.drawable.cheerleader_by_cookiemagik));
		smileys.add(new Smiley("_fluffy__by_cindre.gif",":inlove:","http://www.informatik-forum.at/images/smilies/_fluffy__by_cindre.gif", R.drawable.fluffy__by_cindre));
		smileys.add(new Smiley("Shout",":shout:","http://www.informatik-forum.at/pics/nb/smilies/shout.gif", R.drawable.shout));
		smileys.add(new Smiley("Bounce",":bounce:","http://www.informatik-forum.at/pics/nb/smilies/jump.gif", R.drawable.jump));
		smileys.add(new Smiley("hail",":hail:","http://www.informatik-forum.at/pics/nb/smilies/hail.gif", R.drawable.hail));
		smileys.add(new Smiley("trösten",":troest:","http://www.informatik-forum.at/pics/nb/smilies/troest.gif", R.drawable.troest));
		smileys.add(new Smiley("Trampoline_fun_by_CookiemagiK",":trampolin:","http://www.informatik-forum.at/images/nb/smilies/Trampoline_fun_by_CookiemagiK.gif", R.drawable.trampoline_fun_by_cookiemagik));
		smileys.add(new Smiley("Tasse",":tasse:","http://www.informatik-forum.at/images/nb/smilies/kaffetasse.gif", R.drawable.kaffetasse));
		smileys.add(new Smiley("excited - thanks too CookiemagiK",":excited:","http://www.informatik-forum.at/images/smilies/_excited__by_CookiemagiK.gif", R.drawable.excited__by_cookiemagik));
		smileys.add(new Smiley("Idee",":idea:","http://www.informatik-forum.at/pics/nb/smilies/idea.gif", R.drawable.idea));
		smileys.add(new Smiley("Rofl2",":rofl2:","http://www.informatik-forum.at/pics/nb/smilies/rofl2.gif", R.drawable.rofl2));
		smileys.add(new Smiley("Ghosthour",":gh:","http://www.informatik-forum.at/pics/ob/smilies/ghosthour.gif", R.drawable.ghosthour));
		smileys.add(new Smiley("dance",":dance:","http://www.informatik-forum.at/pics/nb/smilies/dance.gif", R.drawable.dance));
		smileys.add(new Smiley("*angst*",":angst:","http://www.informatik-forum.at/images/nb/smilies/angst.gif", R.drawable.angst));
		smileys.add(new Smiley("turbopoke",":turbopoke:","http://www.informatik-forum.at/images/nb/smilies/turbopoke.gif", R.drawable.turbopoke));
		
		return smileys;
	}
	
	
	public List<Smiley> getAvailableSmileys(){
		return this.smileys;
	}

	
	public Smiley getSmileyByUrl(String url) throws DaoException {
		List<Smiley> smileys = getAvailableSmileys();
		
		for(Smiley s: smileys){
			if(s.getUrl().equals(url)){
				return s;
			}
		}
		
		throw new DaoException(EXCEPTION_SMILEY_NOT_FOUND);
	}
}
