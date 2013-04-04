package com.vimukti.accounter.text;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.vimukti.accounter.text.commands.ITextCommand;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class CommandProcessor {

	private Logger logger = Logger.getLogger(CommandProcessor.class);

	private static CommandProcessor processor;

	public static CommandProcessor getInstance() {
		if (processor == null) {
			processor = new CommandProcessor();
		}
		return processor;
	}

	public ArrayList<CommandResponseImpl> processCommands(CommandsQueue queue,
			String clientEmail) {

		ArrayList<CommandResponseImpl> responses = new ArrayList<CommandResponseImpl>();

		while (queue.hasNext()) {
			CommandResponseImpl cResponse = new CommandResponseImpl();
			processCommand(queue, cResponse, clientEmail);
			responses.add(cResponse);
			if (!cResponse.hasErrors()) {
				cResponse.addMessage("Request successfull!!");
			}
		}

		return responses;
	}

	private void processCommand(CommandsQueue queue,
			CommandResponseImpl response, String clientEmail) {

		// Take next
		ITextData data = queue.take();
		logger.info("Processing command - " + data.toString());
		Class<? extends ITextCommand> commandClass = CommandsFactory
				.getCommand(data.getType());
		ITextCommand command = null;
		try {
			command = commandClass.newInstance();
			CommandContext commandContext = new CommandContext();
			commandContext.put(CommandContext.EMAIL_ID, clientEmail);
			command.setContext(commandContext);
		} catch (ReflectiveOperationException e) {
			// Send response as Invalid command
			response.addError("Invalid command");
		}

		// Parse command
		parseData(command, data, response, queue);

		// Got errors in parsing, don't process this
		if (response.hasErrors()) {
			logger.info("Got errors after parsing command");
			return;
		}

		// PROCESS COMMAND
		try {
			logger.info("Processing command");
			command.process(response);
		} catch (AccounterException e) {
			logger.error("Error while processing command", e);
			// Add Error to response
			response.addError(e.getMessage());
		}

	}

	private void parseData(ITextCommand command, ITextData data,
			CommandResponseImpl response, CommandsQueue queue) {
		logger.info("Parsing Request" + data);
		Class<? extends ITextCommand> commandClass = CommandsFactory
				.getCommand(data.getType());
		if (commandClass != command.getClass()) {
			// If not same command, Just return
			queue.revertPrevious();
			return;
		}

		// Add Data to Response
		response.addData(data);

		// PARSE COMMAND DATA
		boolean isParseSuccess = command.parse(data, response);

		// If Has errors, just return
		if (response.hasErrors()) {
			return;
		}

		// Is Same as Previous, then parse next command
		if (isParseSuccess && queue.hasNext()) {
			parseData(command, queue.take(), response, queue);
		} else {
			// If not same as Previous, then just process it as first data
			// already read by this command
			data.reset();
			if (!isParseSuccess) {
				queue.revertPrevious();
			}
		}
	}
}
