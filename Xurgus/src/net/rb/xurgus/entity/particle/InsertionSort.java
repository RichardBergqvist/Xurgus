package net.rb.xurgus.entity.particle;

import java.util.List;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class InsertionSort {

	public static void sortHighToLow(List<Particle> list) {
		for (int i = 1; i < list.size(); i++) {
			Particle particle = list.get(i);
			if (particle.getDistance() > list.get(i - 1).getDistance())
				sortUpHighToLow(list, i);
		}
	}
	
	private static void sortUpHighToLow(List<Particle> list, int index) {
		Particle particle = list.get(index);
		int attemptPos = index - 1;
		while (attemptPos != 0 && list.get(attemptPos - 1).getDistance() < particle.getDistance())
			attemptPos--;
		list.remove(index);
		list.add(attemptPos, particle);
	}
}