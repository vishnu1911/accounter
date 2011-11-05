/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.List;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MobileChatAdaptor implements MobileAdaptor {

	public static MobileAdaptor INSTANCE = new MobileChatAdaptor();

	/**
	 * PostProcess the Result
	 * 
	 * @param result
	 * @return
	 */
	public String postProcess(Result result, String oldReplay) {
		if (result == null) {
			return null;
		}
		List<Object> resultParts = result.getResultParts();
		StringBuffer reply = new StringBuffer();
		int recordsCount = 1;
		int commandIndex = 97;
		for (Object part : resultParts) {
			if (part instanceof ResultList) {
				ResultList resultList = (ResultList) part;
				for (int x = 0; x < resultList.size(); x++, recordsCount++) {
					Record record = resultList.get(x);
					record.setCode(recordsCount);
					reply.append(record.toString());
				}
			} else if (part instanceof CommandList) {
				CommandList commandList = (CommandList) part;
				for (int x = 0; x < commandList.size(); x++, commandIndex++) {
					reply.append('(');
					reply.append((char) commandIndex);
					reply.append(") ");
					reply.append(commandList.get(x));
					reply.append('\n');
				}
			} else {
				reply.append((String) part);
			}
			reply.append('\n');
		}

		String newReplay = reply.toString();
		if (oldReplay != null && !oldReplay.isEmpty()) {
			newReplay = oldReplay + "\n" + newReplay;
		}
		return newReplay;// .replaceAll("\n", "<>");
	}
}
