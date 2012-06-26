<?php defined('_JEXEC') or die('Restricted access'); ?>

<form action="index.php" method="post" name="adminForm" id="adminForm">
<div class="col100">
	<fieldset class="adminform">
		<legend><?php echo JText::_( 'Delivery Details' ); ?></legend>

		<table class="admintable">
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Database ID' ); ?>:
				</label>
			</td>
			<td>
				<?php echo $this->delivery->delivery_id;
				//dump($this->delivery->delivery_id, "DELID");
				?>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Delivery Date' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="delivery_date" id="delivery_date" size="32" maxlength="250" value="<?php echo $this->delivery->delivery_date;?>" />
				<input type="reset" class="button" value="..." onclick="return showCalendar('delivery_date','%Y-%m-%d');">	
			</td>
		</tr>
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Order Code' ); ?>:
				</label>
			</td>
			<td>
     <?php echo $this->delivery->order_code;?>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Subscriber' ); ?>:
				</label>
			</td>
			<td>
			<?php echo $this->delivery->sub_name;?>
			</td>
		</tr>
		
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Publication' ); ?>:
				</label>
			</td>
			<td>
				<?php echo $this->delivery->pub_name;?>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Delivery Title' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="delivery_title" id="delivery_title" size="32" maxlength="250" value="<?php echo $this->delivery->delivery_title;?>" />
			</td>
		</tr>		
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Issue' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="delivery_issue" id="delivery_issue" size="32" maxlength="250" value="<?php echo $this->delivery->delivery_issue;?>" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Issue Date' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="delivery_issuedt" id="delivery_issuedt" size="32" maxlength="250" value="<?php echo $this->delivery->delivery_issuedt;?>" />
				<input type="reset" class="button" value="..." onclick="return showCalendar('delivery_issuedt','%Y-%m-%d');">	
			</td>
		</tr>
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Copies' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="delivery_copies" id="delivery_copies" size="32" maxlength="250" value="<?php echo $this->delivery->delivery_copies;?>" />
			</td>
		</tr>
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Delivered By' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="delivered_by" id="delivered_by" size="32" maxlength="250" value="<?php echo $this->delivery->delivered_by;?>" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Received By' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="received_by" id="received_by" size="32" maxlength="250" value="<?php echo $this->delivery->received_by;?>" />
			</td>
		</tr>		
	</table>
	</fieldset>
</div>
<div class="clr"></div>

<input type="hidden" name="option" value="com_subsys" />
<input type="hidden" name="delivery_id" value="<?php echo $this->delivery->delivery_id; ?>" />
<input type="hidden" name="task" value="" />
<input type="hidden" name="controller" value="delivery" />
</form>
