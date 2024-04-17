package jagex2.datastruct;

public class Linkable {

    public long id;

    public Linkable prev;

    public Linkable next;

    public final void unlink() {
		if (this.next != null) {
			this.next.prev = this.prev;
			this.prev.next = this.next;
			this.prev = null;
			this.next = null;
		}
	}
}
